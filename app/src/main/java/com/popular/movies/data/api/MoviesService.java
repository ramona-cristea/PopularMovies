package com.popular.movies.data.api;

import com.popular.movies.model.DataWrapper;
import com.popular.movies.model.Movie;
import com.popular.movies.model.Review;
import com.popular.movies.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {
    @GET("movie/{criteria}?")
    Call<DataWrapper<Movie>> getMovies(@Path("criteria") String criteria, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}?")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey,
                                             @Query("append_to_response") String additionalParams);

    @GET("movie/{movie_id}/reviews?")
    Call<DataWrapper<Review>> getMovieReviews(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos?")
    Call<DataWrapper<Trailer>> getMovieTrailers(@Path("movie_id") String movieId, @Query("api_key") String apiKey);
}