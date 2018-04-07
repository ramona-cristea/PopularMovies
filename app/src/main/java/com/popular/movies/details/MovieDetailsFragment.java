package com.popular.movies.details;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular.movies.R;
import com.popular.movies.data.api.MoviesRepositoryImpl;
import com.popular.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment implements UpdateFragmentListener{

    TextView mTextMovieTitle;
    TextView mTextMoviePopularity;
    TextView mTextMovieDuration;
    TextView mTextMovieGenres;
    TextView mTextReleaseDate;
    TextView mTextMovieVote;
    TextView mTextMovieStoryline;
    ImageView mImageMoviePoster;
    CardView mThumbnailCard;
    Movie mMovieDetails;

    MovieDetailsLoadListener mMovieLoadListener;

    DecimalFormat df = new DecimalFormat("#.00");

    public static MovieDetailsFragment newInstance (){
        return new MovieDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mTextMovieTitle = view.findViewById(R.id.text_movie_title);
        mTextReleaseDate = view.findViewById(R.id.text_release_date);
        mTextMovieVote = view.findViewById(R.id.text_vote_average);
        mTextMovieStoryline = view.findViewById(R.id.text_storyline);
        mImageMoviePoster = view.findViewById(R.id.image_movie_thumbnail);
        mTextMoviePopularity = view.findViewById(R.id.text_popularity);
        mTextMovieDuration = view.findViewById(R.id.text_duration);
        mTextMovieGenres = view.findViewById(R.id.text_genres);
        mThumbnailCard = view.findViewById(R.id.card_poster);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMovieLoadListener.loadMovieDetails();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMovieLoadListener = (MovieDetailsLoadListener) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMovieLoadListener = (MovieDetailsLoadListener) activity;
    }

    @Override
    public void updateFragmentWithData(Object movie) {
        if(movie == null) {
            return;
        }
        mMovieDetails = (Movie) movie;
        showMovieDetails();
    }

    private void showMovieDetails() {
        if(mMovieDetails == null) {
            return;
        }
        mThumbnailCard.setVisibility(View.VISIBLE);
        mTextMovieGenres.setVisibility(View.VISIBLE);
        mTextMovieDuration.setVisibility(View.VISIBLE);
        mTextMovieTitle.setText(mMovieDetails.getOriginalTitle());
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = parser.parse(mMovieDetails.getReleaseDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            mTextReleaseDate.setText(getString(R.string.release, year));
        } catch (ParseException e) {
            e.printStackTrace();
            mTextReleaseDate.setText(mMovieDetails.getReleaseDate());
        }

        mTextMovieVote.setText(getString(R.string.movie_vote, Double.toString(mMovieDetails.getVoteAverage())));
        mTextMovieDuration.setText(getString(R.string.duration_in_min, mMovieDetails.getRuntime()));
        mTextMoviePopularity.setText(getString(R.string.popularity, df.format(mMovieDetails.getPopularity())));
        mTextMovieGenres.setText(mMovieDetails.getGenres());
        mTextMovieStoryline.setText(mMovieDetails.getOverview());

        String urlBuilder = MoviesRepositoryImpl.BASE_URL_IMAGES +
                mMovieDetails.getPosterPath();
        Picasso.get()
                .load(urlBuilder)
                .placeholder(R.drawable.vector_placeholder_poster)
                .error(R.drawable.vector_error)
                .into(mImageMoviePoster);
    }

    public interface MovieDetailsLoadListener {
        void loadMovieDetails();
    }
}
