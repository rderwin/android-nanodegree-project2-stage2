package com.under_rated.popularmoviesretrofit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.under_rated.popularmoviesretrofit.API.ResultsHelper;
import com.under_rated.popularmoviesretrofit.Model.Movie;
import com.under_rated.popularmoviesretrofit.Model.ResultsPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by dave on 7/16/15.
 */
public class MovieGridFragment extends Fragment {
    private final static String LOG_TAG = "MovieGridFragment";
    private final static String MOVIES_LIST_KEY = "Movies";

    private MovieListAdapter movieListAdapter;
    private ArrayList<Movie> movieList;
    private String currentSortOrder = "";

    public MovieGridFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(MOVIES_LIST_KEY, movieListAdapter.getMovies());
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_LIST_KEY)) {
            movieList = savedInstanceState.getParcelableArrayList(MOVIES_LIST_KEY);
        } else {
            movieList = new ArrayList<Movie>();
        }

        movieListAdapter =
                new MovieListAdapter (
                        getActivity(),
                        R.layout.grid_item_movie_fragment,
                        movieList);

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Movie selectedMovie = movieListAdapter.getItem(position);
                ((MovieGridCallback) getActivity()).onItemSelected(selectedMovie);
            }
        });
        gridView.setAdapter(movieListAdapter);

        return rootView;
    }

    public interface MovieGridCallback {
        public void onItemSelected(Movie movie);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default));

        if (movieList.size() == 0 || !sortOrder.equals(this.currentSortOrder)) {
            this.currentSortOrder = sortOrder;
            if (this.currentSortOrder.equals(getString(R.string.pref_sort_order_favorites_key))) {
                movieListAdapter.clear();
                Set<String> favorites = prefs.getStringSet(getString(R.string.pref_favorites_key), new HashSet<String>());
                for (String movieId : favorites) {
                    loadMovie(movieId);
                }
            } else {
                updateMovies(this.currentSortOrder);
            }
        }
    }

    private void updateMovies(String sortOrder) {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("https://api.themoviedb.org/3").build();
        ResultsHelper resultsHelper = restadapter.create(ResultsHelper.class);

        resultsHelper.discoverMovie(ResultsHelper.api_key, sortOrder, new Callback<ResultsPage>() {
            @Override
            public void success(ResultsPage results, Response response) {
                if (results != null) {
                    movieListAdapter.clear();
                    for(Movie movie : results.getResults()) {
                        movieListAdapter.add(movie);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.toString());
            }
        });
    }

    private void loadMovie(String movieId) {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("https://api.themoviedb.org/3").build();
        ResultsHelper resultsHelper = restadapter.create(ResultsHelper.class);

        resultsHelper.getMovie(Integer.valueOf(movieId), ResultsHelper.api_key, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                movieListAdapter.add(movie);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.toString());
            }
        });
    }



}
