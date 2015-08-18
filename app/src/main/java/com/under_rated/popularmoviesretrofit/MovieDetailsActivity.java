package com.under_rated.popularmoviesretrofit;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by dave on 8/17/15.
 */
public class MovieDetailsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailsFragment())
                    .commit();
        }
    }
}
