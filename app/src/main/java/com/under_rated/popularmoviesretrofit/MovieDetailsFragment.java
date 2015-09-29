package com.under_rated.popularmoviesretrofit;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.under_rated.popularmoviesretrofit.API.ResultsHelper;
import com.under_rated.popularmoviesretrofit.Model.Movie;
import com.under_rated.popularmoviesretrofit.Model.Review;
import com.under_rated.popularmoviesretrofit.Model.ReviewResultsPage;
import com.under_rated.popularmoviesretrofit.Model.Video;
import com.under_rated.popularmoviesretrofit.Model.VideoResultsPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dave on 8/17/15.
 */
public class MovieDetailsFragment extends Fragment {
    private final static String LOG_TAG = "MovieDetailsFragment";

    private VideoListAdapter videoListAdapter;
    private ArrayList<Video> videoList;
    private ReviewListAdapter reviewListAdapter;
    private ArrayList<Review> reviewList;

    private ListView videoListView;
    private ListView reviewListView;
    private Set<String> favorites;

    private Movie movie;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putParcelableArrayList(MOVIES_LIST_KEY, movieListAdapter.getMovies());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        videoList = new ArrayList<Video>();
        reviewList = new ArrayList<Review>();

        movie = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable("movie");
        } else {
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("movie")) {
                movie = intent.getParcelableExtra("movie");
            }
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        favorites = prefs.getStringSet(getString(R.string.pref_favorites_key),
                new HashSet<String>());


        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        FrameLayout framelayout_movie_details = (FrameLayout) rootView.findViewById(R.id.framelayout_movie_details);
        TextView movieTitle = (TextView) rootView.findViewById(R.id.textview_movie_title);
        TextView movieDescription = (TextView) rootView.findViewById(R.id.textview_movie_description);
        TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.textview_movie_release_date);
        TextView movieRating = (TextView) rootView.findViewById(R.id.textview_movie_rating);
        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.imageview_movie_poster);
        final Button button_mark_as_favorite = (Button) rootView.findViewById(R.id.button_mark_as_favorite);

        if (movie != null) {
            framelayout_movie_details.setVisibility(View.VISIBLE);
            movieTitle.setText(movie.getTitle());
            movieDescription.setText(movie.getOverview());

            if (movie.getRelease_date() != null && movie.getRelease_date().length() > 4) {
                movieReleaseDate.setText(movie.getRelease_date().substring(0, 4));
            } else {
                movieReleaseDate.setText("N/A");
            }

            movieRating.setText(movie.getVote_average() + "/10");
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" +
                    movie.getPoster_path()).into(moviePoster);

            if (favorites.contains(String.valueOf(movie.getId()))) {
                button_mark_as_favorite.setText(getString(R.string.favorited));
            }

            button_mark_as_favorite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (button_mark_as_favorite.getText().equals(getString(R.string.mark_as_favorite))) {
                        button_mark_as_favorite.setText(getString(R.string.favorited));
                        favorites.add(String.valueOf(movie.getId()));
                        prefs.edit().putStringSet(getString(R.string.pref_favorites_key), favorites).commit();
                    } else {
                        button_mark_as_favorite.setText(getString(R.string.mark_as_favorite));
                        favorites.remove(String.valueOf(movie.getId()));
                        prefs.edit().putStringSet(getString(R.string.pref_favorites_key), favorites).commit();
                    }
                }
            });

            videoListAdapter =
                    new VideoListAdapter(
                            getActivity(),
                            R.layout.list_item_video_fragment,
                            videoList);

            videoListView = (ListView) rootView.findViewById(R.id.video_list);
            videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Video selectedVideo = videoListAdapter.getItem(position);
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("vnd.youtube:" + selectedVideo.getKey()));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + selectedVideo.getKey()));
                        startActivity(intent);
                    }
                }
            });
            videoListView.setAdapter(videoListAdapter);
            setListViewHeightBasedOnChildren(videoListView);

            reviewListAdapter =
                    new ReviewListAdapter(
                            getActivity(),
                            R.layout.list_item_review_fragment,
                            reviewList);

            reviewListView = (ListView) rootView.findViewById(R.id.review_list);
            reviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Review selectedReview = reviewListAdapter.getItem(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(selectedReview.getUrl()));
                    startActivity(browserIntent);
                }
            });
            reviewListView.setAdapter(reviewListAdapter);
            setListViewHeightBasedOnChildren(reviewListView);

            loadVideos(movie.getId());
            loadReviews(movie.getId());
        } else {
            framelayout_movie_details.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }

    private void loadVideos(int id) {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("https://api.themoviedb.org/3").build();
        ResultsHelper resultsHelper = restadapter.create(ResultsHelper.class);

        resultsHelper.getVideos(id, ResultsHelper.api_key, new Callback<VideoResultsPage>() {

            @Override
            public void success(VideoResultsPage results, Response response) {
                if (results != null) {
                    videoListAdapter.clear();
                    for (Video video : results.getResults()) {
                        videoListAdapter.add(video);
                    }
                }
                setListViewHeightBasedOnChildren(videoListView);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.toString());
            }
        });
    }

    private void loadReviews(int id) {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("https://api.themoviedb.org/3").build();
        ResultsHelper resultsHelper = restadapter.create(ResultsHelper.class);

        resultsHelper.getReviews(id, ResultsHelper.api_key, new Callback<ReviewResultsPage>() {

            @Override
            public void success(ReviewResultsPage results, Response response) {
                if (results != null) {
                    reviewListAdapter.clear();
                    for (Review review : results.getResults()) {
                        reviewListAdapter.add(review);
                    }
                }
                setListViewHeightBasedOnChildren(reviewListView);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.toString());
            }
        });
    }

    /****
     * Code from http://stackoverflow.com/questions/3495890/how-can-i-put-a-listview-into-a-scrollview-without-it-collapsing
     * By user XYZ
     *
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
