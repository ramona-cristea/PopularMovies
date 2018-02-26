package com.popular.movies.api;

import android.arch.lifecycle.LiveData;

import com.popular.movies.model.MoviesWrapper;

public interface MoviesRepository {
    LiveData<MoviesWrapper> getMovies(String criteria);
}
