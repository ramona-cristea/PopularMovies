package com.popular.movies.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.popular.movies.data.api.MoviesRepository;
import com.popular.movies.data.api.MoviesRepositoryImpl;

public class MovieDetailsViewModel extends ViewModel{
    private MediatorLiveData<Movie> mMovieLiveData;
    private MoviesRepository mMoviesRepository;

    public MovieDetailsViewModel() {
        mMovieLiveData = new MediatorLiveData<>();
        mMoviesRepository = new MoviesRepositoryImpl();
    }

    @NonNull
    public LiveData<Movie> getMovieDetails() {
        return mMovieLiveData;
    }

    public void loadMovieDetails(int movieId) {
        mMovieLiveData.addSource(
                mMoviesRepository.getMovieDetails(movieId),
                moviesWrapper -> mMovieLiveData.setValue(moviesWrapper)
        );
    }
}
