package com.example.marina.phase1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.List;

/**
 * Created by Marina on 3/31/2016.
 */
public class MainActivityFragment extends Fragment {
    private MovieAdapter movie_adpt;
    public List<Movie> list_movie;
private  MovieInterface mov_interface;
    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.mov_interface = (MovieInterface) activity;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences obj = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String PrefValue = obj.getString("settingsKey", "popular");

        if (PrefValue.equals("favourites")) {
            DetailsFragment.favourites = obj.getStringSet("favKey", null);
//            Log.d("wess",DetailsFragment.favourites.toString());
            if (DetailsFragment.favourites != null) {
                try {

                    list_movie = parsed_fav(DetailsFragment.favourites.toString());
                movie_adpt.setM(list_movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            MoviesTask task = new MoviesTask();
            task.execute(PrefValue);
        }

    }

    public List<Movie> parsed_fav(String json) throws JSONException {
        List<Movie> newlist = new ArrayList<Movie>();

        final String release_date = "release_date";
        final String original_title = "original_title";
        final String vote_average = "vote_average";
        final String poster = "poster_path";
        final String overview = "overview";
        final String id = "id";


        JSONArray Jarr = new JSONArray(json);


        for (int i = 0; i < Jarr.length(); i++) {

            JSONObject Jobj_movie = Jarr.getJSONObject(i);
            Movie m_obj = new Movie();

            m_obj.setOriginal_title(Jobj_movie.getString(original_title));
            m_obj.setOverview(Jobj_movie.getString(overview));
            m_obj.setPoster(Jobj_movie.getString(poster));
            m_obj.setRelease_date(Jobj_movie.getString(release_date));
            m_obj.setVote_average(Jobj_movie.getString(vote_average));
            m_obj.setID(Jobj_movie.getString(id));

            newlist.add(m_obj);

        }
        return newlist;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        GridView gridview = (GridView) view.findViewById(R.id.imageGrid);

        movie_adpt = new MovieAdapter(this.getActivity());
        gridview.setAdapter(movie_adpt);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie m = (Movie) movie_adpt.getItem(i);
                mov_interface.choosenMovie(m);

            }
        });

        return view;

    }


    public class MoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override

        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            movie_adpt.setM(movies);

        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> mymovies = null;

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

                final String MOVIE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "?";
                final String key = "api_key";
                String myapikey = "";
                Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                        .appendQueryParameter(key, myapikey)
                        .build();
                Log.d("Movies in JSON", "JSON = " + builtUri);

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

                Log.d("Movies in JSON", "JSON = " + result_JSON);

                mymovies = parseJson(result_JSON);
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

            return mymovies;
        }

        public List<Movie> parseJson(String json) throws JSONException {
            List<Movie> newlist = new ArrayList<Movie>();

            final String release_date = "release_date";
            final String original_title = "original_title";
            final String vote_average = "vote_average";
            final String poster = "poster_path";
            final String overview = "overview";
            final String id = "id";

            JSONObject Jobj = new JSONObject(json);

            JSONArray Jarr = Jobj.getJSONArray("results");

            //Log.d("Size 2", Jarr.length()+" ");

            for (int i = 0; i < Jarr.length(); i++) {
                ///Log.d("Size ", "Helloo");
                JSONObject Jobj_movie = Jarr.getJSONObject(i);

                Movie m_obj = new Movie();

                m_obj.setOriginal_title(Jobj_movie.getString(original_title));
                m_obj.setOverview(Jobj_movie.getString(overview));
                m_obj.setPoster(Jobj_movie.getString(poster));
                m_obj.setRelease_date(Jobj_movie.getString(release_date));
                m_obj.setVote_average(Jobj_movie.getString(vote_average));
                m_obj.setID(Jobj_movie.getString(id));

                newlist.add(m_obj);
            }

            //Log.d("Size 2", newlist.size()+" ");

            return newlist;
        }
    }
}
