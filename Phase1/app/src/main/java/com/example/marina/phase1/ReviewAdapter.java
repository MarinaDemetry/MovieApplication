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
public class ReviewAdapter extends ArrayAdapter<Reviews> {
    List<Reviews> reviews;
    Context c;
    LayoutInflater inflater;

    public ReviewAdapter(Context context) {
        super(context, 0);
        this.c = context;
        this.inflater = LayoutInflater.from(context);
        this.reviews = new ArrayList<Reviews>();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }


    public void setR(List<Reviews> reviews_list) {
        this.reviews.clear();
        this.reviews.addAll(reviews_list);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.review, parent, false);
        TextView review_auth = (TextView) view.findViewById(R.id.author);
        review_auth.setText(reviews.get(position).getAuthor());

        TextView review_cont = (TextView) view.findViewById(R.id.content);
        review_auth.setText(reviews.get(position).getContent());


        return view;

    }
}
