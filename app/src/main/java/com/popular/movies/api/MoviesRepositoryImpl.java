package com.popular.movies.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.popular.movies.model.MoviesWrapper;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepositoryImpl implements MoviesRepository {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p/w342/";
    private MoviesService mMoviesService;

    public MoviesRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        mMoviesService = retrofit.create(MoviesService.class);
    }

    private static String getApiKey(){
        byte[] data = Base64.decode("YOUR_API_KEY", Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public LiveData<MoviesWrapper> getMovies(String criteria) {
        final MutableLiveData<MoviesWrapper> moviesLiveData = new MutableLiveData<>();
        Call<MoviesWrapper> request = mMoviesService.getMovies(criteria, getApiKey());
        request.enqueue(new Callback<MoviesWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MoviesWrapper> call, @NonNull Response<MoviesWrapper> response) {
                moviesLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesWrapper> call, @NonNull Throwable error) {
                moviesLiveData.setValue(null);
            }
        });


        return moviesLiveData;
    }
}
