package com.example.marina.phase1;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Marina on 4/25/2016.
 */
public class DetailsFragment extends Fragment {
    private TextView titleTextView;
    private ImageView imageView;
    private ListView trailerView;
    private ListView reviewView;
    private TrailerAdapter trail_adpt;
    private ReviewAdapter review_adpt;
    private Movie movie;
    public static Set<String> favourites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        movie = (Movie) extras.getSerializable("MovieKey");

    }

    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        TrailerTask asyncObj = new TrailerTask();
        ReviewTask asyncReview = new ReviewTask();

        imageView = (ImageView) view.findViewById(R.id.filmImage);
        ImageView fav = (ImageView) view.findViewById(R.id.fav);

        titleTextView = (TextView) view.findViewById(R.id.film_name);
        titleTextView.setText(movie.getOriginal_title());
        titleTextView = (TextView) view.findViewById(R.id.rating);
        titleTextView.setText(movie.getVote_average());
        titleTextView = (TextView) view.findViewById(R.id.Description);
        titleTextView.setText(movie.getOverview());
        titleTextView = (TextView) view.findViewById(R.id.filmYear);
        titleTextView.setText(movie.getRelease_date());

        trailerView = (ListView) view.findViewById(R.id.trailers);
        reviewView = (ListView) view.findViewById(R.id.reviews);


        trail_adpt = new TrailerAdapter(getActivity());
        // trail_adpt.setT(trailers);
        trailerView.setAdapter(trail_adpt);

        review_adpt = new ReviewAdapter(getActivity());
        // trail_adpt.setT(trailers);

        reviewView.setAdapter(review_adpt);
        asyncObj.execute(movie.getID());
        asyncReview.execute(movie.getID());


        trailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://www.youtube.com/watch?v=" + trail_adpt.getItem(i).getKey())));

            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {

                if (favourites == null)
                    favourites = new HashSet<String>();

                try {
                    favourites.add(movie.toJson().toString());
                    Log.d("wes", favourites.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putStringSet("favKey", favourites).commit();

            }
        });

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster()).into(imageView);

        return view;

    }


    public class TrailerTask extends AsyncTask<String, Void, List<Trailer>> {

        @Override

        protected void onPostExecute(List<Trailer> trailers) {

            trail_adpt.setT(trailers);


        }

        @Override
        protected List<Trailer> doInBackground(String... params) {
            List<Trailer> mytrailers = null;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result_JSON = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String Trailer_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?";
                final String key = "api_key";
                String myapikey = "";
                Uri builtUri = Uri.parse(Trailer_URL).buildUpon()
                        .appendQueryParameter(key, myapikey)
                        .build();
                // Log.d("Movies in JSON", "JSON = " + builtUri);

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                result_JSON = buffer.toString();

//                Log.d("Movies in JSON", "JSON = " + result_JSON);

                mytrailers = parseJson(result_JSON);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return mytrailers;
        }

        public List<Trailer> parseJson(String json) throws JSONException {
            List<Trailer> newlist = new ArrayList<Trailer>();

            final String key = "key";
            final String name = "name";

            JSONObject Jobj = new JSONObject(json);

            JSONArray Jarr = Jobj.getJSONArray("results");

            //Log.d("Size 2", Jarr.length()+" ");

            for (int i = 0; i < Jarr.length(); i++) {
                ///Log.d("Size ", "Helloo");
                JSONObject Jobj_movie = Jarr.getJSONObject(i);

                Trailer t_obj = new Trailer();

                t_obj.setName(Jobj_movie.getString(name));
                t_obj.setKey(Jobj_movie.getString(key));

                newlist.add(t_obj);
            }

            //Log.d("Size 2", newlist.size()+" ");

            return newlist;
        }
    }

    public class ReviewTask extends AsyncTask<String, Void, List<Reviews>> {


        @Override

        protected void onPostExecute(List<Reviews> review) {


            review_adpt.setR(review);


        }

        @Override
        protected List<Reviews> doInBackground(String... params) {
            List<Reviews> myReviews = null;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result_JSON = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String Reviews_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?";
                final String key = "api_key";
                String myapikey = "";
                Uri builtUri = Uri.parse(Reviews_URL).buildUpon()
                        .appendQueryParameter(key, myapikey)
                        .build();
                // Log.d("Movies in JSON", "JSON = " + builtUri);

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                result_JSON = buffer.toString();

//                Log.d("Movies in JSON", "JSON = " + result_JSON);

                myReviews = parseJson(result_JSON);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return myReviews;
        }

        public List<Reviews> parseJson(String json) throws JSONException {
            List<Reviews> newlist = new ArrayList<Reviews>();

            final String content = "content";
            final String author = "author";

            JSONObject Jobj = new JSONObject(json);

            JSONArray Jarr = Jobj.getJSONArray("results");

            //Log.d("Size 2", Jarr.length()+" ");

            for (int i = 0; i < Jarr.length(); i++) {
                ///Log.d("Size ", "Helloo");
                JSONObject Jobj_review = Jarr.getJSONObject(i);

                Reviews r_obj = new Reviews();

                r_obj.setContent(Jobj_review.getString(content));
                r_obj.setAuthor(Jobj_review.getString(author));

                newlist.add(r_obj);
            }

            //Log.d("Size 2", newlist.size()+" ");

            return newlist;
        }
    }
}
