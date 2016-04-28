package com.example.marina.phase1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 4/25/2016.
 */
public class MovieAdapter extends BaseAdapter {
    private List<Movie> m ;
    private Context context;
    private LayoutInflater lay ;


    public MovieAdapter(Context c ) {
        this.context = c;
        this.lay = LayoutInflater.from(c);
        this.m = new ArrayList<>();

    }

    public void setM(List<Movie> m) {
      this.m.clear();
      this.m. addAll(m) ;
        notifyDataSetChanged();
        //Log.d("hello", m.size()+ " ");


    }

    @Override
    public int getCount() {

        return m.size();
    }

    @Override
    public Object getItem(int i) {
        return m.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView movieImage;
        if (view == null) {

            // if it's not recycled, initialize some attributes
            view = lay.inflate(R.layout.movie_img,viewGroup,false);
            movieImage = (ImageView) view.findViewById(R.id.imageMovie);
           // Log.d("hiii", m.get(i).getPoster());


        } else {

            movieImage = (ImageView) view;
          //  Log.d("hiii", m.get(i).getPoster());
        }


        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + m.get(i).getPoster()).into(movieImage);
        movieImage.setScaleType(ImageView.ScaleType.FIT_XY);

        return movieImage;

    }
}
