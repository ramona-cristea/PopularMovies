package com.popular.movies.data.api;

import android.arch.lifecycle.LiveData;

import com.popular.movies.model.DataWrapper;
import com.popular.movies.model.Movie;
import com.popular.movies.model.Review;
import com.popular.movies.model.Trailer;

public interface MoviesRepository {
    LiveData<DataWrapper<Movie>> getMovies(String criteria);

    LiveData<DataWrapper<Review>> getMovieReviews(String movieId);

    LiveData<DataWrapper<Trailer>> getMovieTrailers(String movieId);

    LiveData<Movie> getMovieDetails(int movieId);
}
