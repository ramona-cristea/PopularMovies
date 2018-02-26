package com.popular.movies.details;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular.movies.R;
import com.popular.movies.api.MoviesRepositoryImpl;
import com.popular.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView textMovieTitle = findViewById(R.id.text_movie_title);
        TextView textReleaseDate = findViewById(R.id.text_release_date);
        TextView textMovieVote = findViewById(R.id.text_vote_average);
        TextView textMovieOverview = findViewById(R.id.text_overview);
        ImageView imageMovieThumbnail = findViewById(R.id.image_movie_thumbnail);
        Intent intent = getIntent();
        if(intent!= null && intent.hasExtra(EXTRA_MOVIE)) {
            Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);

            textMovieTitle.setText(movie.getOriginalTitle());
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date date = parser.parse(movie.getReleaseDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                textReleaseDate.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            } catch (ParseException e) {
                e.printStackTrace();
                textReleaseDate.setText(movie.getReleaseDate());
            }

            textMovieVote.setText(getString(R.string.movie_vote, Double.toString(movie.getVoteAverage())));
            textMovieOverview.setText(movie.getOverview());
            String urlBuilder = MoviesRepositoryImpl.BASE_URL_IMAGES +
                    movie.getPosterPath();
            Picasso.with(imageMovieThumbnail.getContext())
                    .load(urlBuilder)
                    .resize(getResources().getDimensionPixelSize(R.dimen.thumbnail_width),
                            getResources().getDimensionPixelSize(R.dimen.thumbnail_height))
                    .placeholder(R.drawable.vector_placeholder_poster)
                    .error(R.drawable.vector_error)
                    .centerCrop()
                    .into(imageMovieThumbnail);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
