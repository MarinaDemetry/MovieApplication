package com.example.marina.phase1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 4/27/2016.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    List<Trailer> trailers;
    Context c;
    LayoutInflater inflater;

    @Override
    public Trailer getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    public TrailerAdapter(Context context) {
        super(context, 0);
        this.c = context;
        this.trailers = new ArrayList<Trailer>();
        this.inflater = LayoutInflater.from(c);
       // Log.d("wess", trailers.get(0).getName() + " ");
    }

    public void setT(List<Trailer> trailer_list) {
        this.trailers.clear();
        this.trailers.addAll(trailer_list);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.trailer, parent, false);
        TextView trailer_name = (TextView) view.findViewById(R.id.trailerID);
        trailer_name.setText(trailers.get(position).getName());


        return view;

    }
}

