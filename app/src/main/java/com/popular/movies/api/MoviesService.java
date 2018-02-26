package com.popular.movies.api;

import com.popular.movies.model.MoviesWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {
    @GET("movie/{criteria}?")
    Call<MoviesWrapper> getMovies(@Path("criteria") String criteria, @Query("api_key") String apiKey);
}