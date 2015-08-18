package com.under_rated.popularmoviesretrofit.API;

import com.under_rated.popularmoviesretrofit.Model.ResultsPage;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by dave on 8/3/15.
 */
public interface ResultsHelper {

    public static final String api_key = "ENTER_API_KEY_HERE";

    @GET("/discover/movie")
    public void discoverMovie(@Query("api_key") String api_key,
                              @Query("sort_by") String sort_by,
                              Callback<ResultsPage> response);

}
