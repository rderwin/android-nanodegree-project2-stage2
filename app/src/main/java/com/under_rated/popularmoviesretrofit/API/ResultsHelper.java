package com.under_rated.popularmoviesretrofit.API;

import com.under_rated.popularmoviesretrofit.Model.Movie;
import com.under_rated.popularmoviesretrofit.Model.ResultsPage;
import com.under_rated.popularmoviesretrofit.Model.ReviewResultsPage;
import com.under_rated.popularmoviesretrofit.Model.VideoResultsPage;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by dave on 8/3/15.
 */
public interface ResultsHelper {

    public static final String api_key = "INSERT_API_KEY_HERE";

    @GET("/discover/movie")
    public void discoverMovie(@Query("api_key") String api_key,
                              @Query("sort_by") String sort_by,
                              Callback<ResultsPage> response);

    @GET("/movie/{id}/videos")
    public void getVideos(@Path("id") int id,
                          @Query("api_key") String api_key,
                          Callback<VideoResultsPage> response);

    @GET("/movie/{id}/reviews")
    public void getReviews(@Path("id") int id,
                           @Query("api_key") String api_key,
                           Callback<ReviewResultsPage> response);

    @GET("/movie/{id}")
    public void getMovie(@Path("id") int id,
                           @Query("api_key") String api_key,
                           Callback<Movie> response);


}
