package com.under_rated.popularmoviesretrofit.Model;

import java.util.List;

/**
 * Created by dave on 9/23/15.
 */
public class VideoResultsPage {
    private int id;
    private List<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
