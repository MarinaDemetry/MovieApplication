package com.example.marina.phase1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Marina on 3/31/2016.
 */
public class Movie implements Serializable {

    private String original_title;
    private String release_date;
    private String overview;
    private String poster;
    private String vote_average;
    private String ID;

    public Movie() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public JSONObject toJson() throws JSONException {
        final String release_date = "release_date";
        final String original_title = "original_title";
        final String vote_average = "vote_average";
        final String poster = "poster_path";
        final String overview = "overview";
        final String id = "id";

        JSONObject movie = new JSONObject();

        movie.put(release_date, this.release_date);
        movie.put(original_title, this.original_title);
        movie.put(vote_average, this.vote_average);
        movie.put(poster, this.poster);
        movie.put(overview, this.overview);
        movie.put(id, this.ID);

        return movie;
    }

}
