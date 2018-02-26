package com.popular.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesWrapper {

    @SerializedName("results")
    private List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
    }
}
