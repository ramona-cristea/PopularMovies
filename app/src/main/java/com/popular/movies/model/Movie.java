package com.popular.movies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable{
    //TODO change Movie class in order to handle properties from GetMovieDetails method
    //E.g https://api.themoviedb.org/3/movie/269149?api_key=eb37d8830354727623a185f6f8c5c616&append_to_response=videos,reviews

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("id")
    private int id;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("title")
    private String title;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("genres")
    private List<Genre> genres = null;

    private String genresAsString;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("videos")
    private DataWrapper<Trailer> videos;

    @SerializedName("reviews")
    private DataWrapper<Review> reviews;

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Movie(){}

    private Movie(Parcel in) {
        voteCount = in.readInt();
        id = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        genresAsString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getRuntime() {
        return runtime;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(voteCount);
        out.writeInt(id);
        out.writeByte((byte) (video ? 1 : 0));
        out.writeDouble(voteAverage);
        out.writeString(title);
        out.writeDouble(popularity);
        out.writeString(posterPath);
        out.writeString(originalLanguage);

        out.writeString(originalTitle);
        out.writeString(backdropPath);
        out.writeString(overview);
        out.writeString(releaseDate);
        out.writeString(genresAsString);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getGenres() {
        StringBuilder genresBuilder = new StringBuilder();
        if(genres != null && genres.size() > 0) {
            for(int i = 0; i < genres.size(); i++) {
                genresBuilder.append(genres.get(i).getName());
                if(i < genres.size() - 1) {
                    genresBuilder.append(", ");
                }
            }
        }
        if(!TextUtils.isEmpty(genresBuilder.toString())) {
            genresAsString = genresBuilder.toString();
        }
        return genresAsString;
    }

    public DataWrapper<Trailer> getVideos() {
        return videos;
    }

    public DataWrapper<Review> getReviews() {
        return reviews;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setGenresAsString(String genresAsString) {
        this.genresAsString = genresAsString;
    }
}
