package com.popular.movies.data.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.popular.movies.model.DataWrapper;
import com.popular.movies.model.Movie;
import com.popular.movies.model.Review;
import com.popular.movies.model.Trailer;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepositoryImpl implements MoviesRepository {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p/w500/";
    public static final String BASE_URL_TRAILER_THUMBNAIL = "https://img.youtube.com/vi/video_key/mqdefault.jpg";

    private static final String ADDITIONAL_MOVIE_PARAMS = "videos,reviews";
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
    public LiveData<DataWrapper<Movie>> getMovies(String criteria) {
        final MutableLiveData<DataWrapper<Movie>> moviesLiveData = new MutableLiveData<>();
        Call<DataWrapper<Movie>> request = mMoviesService.getMovies(criteria, getApiKey());
        request.enqueue(new Callback<DataWrapper<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<DataWrapper<Movie>> call, @NonNull Response<DataWrapper<Movie>> response) {
                moviesLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DataWrapper<Movie>> call, @NonNull Throwable error) {
                moviesLiveData.setValue(null);
            }
        });

        return moviesLiveData;
    }

    @Override
    public LiveData<DataWrapper<Review>> getMovieReviews(String movieId) {
        final MutableLiveData<DataWrapper<Review>> reviewsLiveData = new MutableLiveData<>();
        Call<DataWrapper<Review>> request = mMoviesService.getMovieReviews(movieId, getApiKey());
        request.enqueue(new Callback<DataWrapper<Review>>() {
            @Override
            public void onResponse(@NonNull Call<DataWrapper<Review>> call, @NonNull Response<DataWrapper<Review>> response) {
                reviewsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DataWrapper<Review>> call, @NonNull Throwable error) {
                reviewsLiveData.setValue(null);
            }
        });

        return reviewsLiveData;
    }

    @Override
    public LiveData<DataWrapper<Trailer>> getMovieTrailers(String movieId) {
        final MutableLiveData<DataWrapper<Trailer>> trailersLiveData = new MutableLiveData<>();
        Call<DataWrapper<Trailer>> request = mMoviesService.getMovieTrailers(movieId, getApiKey());
        request.enqueue(new Callback<DataWrapper<Trailer>>() {
            @Override
            public void onResponse(@NonNull Call<DataWrapper<Trailer>> call, @NonNull Response<DataWrapper<Trailer>> response) {
                trailersLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DataWrapper<Trailer>> call, @NonNull Throwable error) {
                trailersLiveData.setValue(null);
            }
        });

        return trailersLiveData;
    }

    @Override
    public LiveData<Movie> getMovieDetails(int movieId) {
        final MutableLiveData<Movie> movieDetailsLiveData = new MutableLiveData<>();
        Call<Movie> request = mMoviesService.getMovieDetails(movieId, getApiKey(), ADDITIONAL_MOVIE_PARAMS);
        request.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                movieDetailsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable error) {
                movieDetailsLiveData.setValue(null);
            }
        });

        return movieDetailsLiveData;
    }
}
