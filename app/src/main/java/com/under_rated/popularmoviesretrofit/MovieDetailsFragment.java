package com.under_rated.popularmoviesretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by dave on 8/17/15.
 */
public class MovieDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        TextView movieTitle = (TextView) rootView.findViewById(R.id.textview_movie_title);
        TextView movieInfo = (TextView) rootView.findViewById(R.id.textview_movie_info);
        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.imageview_movie_poster);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TITLE) &&
                intent.hasExtra(Intent.EXTRA_TEXT) && intent.hasExtra(Intent.EXTRA_HTML_TEXT)) {
            String movieTitleString = intent.getStringExtra(Intent.EXTRA_TITLE);
            String movieInfoString = intent.getStringExtra(Intent.EXTRA_TEXT);
            String moviePosterString = intent.getStringExtra(Intent.EXTRA_HTML_TEXT);
            movieTitle.setText(movieTitleString);
            movieInfo.setText(movieInfoString);
            Picasso.with(getActivity()).load(moviePosterString).into(moviePoster);
        }

        return rootView;
    }




}
