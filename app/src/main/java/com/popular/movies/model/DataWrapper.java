package com.popular.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataWrapper<Data> {

    @SerializedName("results")
    private List<Data> results = null;

    public List<Data> getResults() {
        return results;
    }
}
