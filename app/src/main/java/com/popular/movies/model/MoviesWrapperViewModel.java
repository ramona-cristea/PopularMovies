package com.popular.movies.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.popular.movies.api.MoviesRepository;
import com.popular.movies.api.MoviesRepositoryImpl;

public class MoviesWrapperViewModel extends ViewModel{
    private MediatorLiveData<MoviesWrapper> mMoviesWrapper;
    private MoviesRepository mMoviesRepository;

    public MoviesWrapperViewModel() {
        mMoviesWrapper = new MediatorLiveData<>();
        mMoviesRepository = new MoviesRepositoryImpl();
    }

    @NonNull
    public LiveData<MoviesWrapper> getMoviesWrapper() {
        return mMoviesWrapper;
    }

    public void loadMovies(@NonNull String criteria) {
        mMoviesWrapper.addSource(
                mMoviesRepository.getMovies(criteria),
                moviesWrapper -> mMoviesWrapper.setValue(moviesWrapper)
        );
    }
}
