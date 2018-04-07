package com.popular.movies.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.popular.movies.R;
import com.popular.movies.data.database.FavoriteContract;
import com.popular.movies.model.Movie;
import com.popular.movies.model.MovieDetailsViewModel;

public class MovieDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, MovieDetailsFragment.MovieDetailsLoadListener {
    MovieDetailsViewModel mMovieViewModel;
    DisabledSwipeViewPager mViewPager;
    MovieDetailsFragmentPagerAdapter mPagerAdapter;
    Movie mMovieDetails;
    boolean addedToFavorites = false;
    ProgressBar mLoadingIndicator;

    public static final String EXTRA_MOVIE_ID = "movie_details";
    public static final String EXTRA_FAVORITE_DATA = "movie_favorite_data";
    private static final String EXTRA_FAVORITE_FLAG = "movie_is_favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.movie);
        }

        mLoadingIndicator = findViewById(R.id.progress_bar_details);
        mViewPager = findViewById(R.id.view_pager_movie);
        mViewPager.setPagingEnabled(false);
        mViewPager.setOffscreenPageLimit(3);
        String [] tabsTitles = getResources().getStringArray(R.array.movie_details_sections);
        mPagerAdapter = new MovieDetailsFragmentPagerAdapter(getSupportFragmentManager(), tabsTitles);
        mViewPager.setAdapter(mPagerAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    int position;
                    switch (item.getItemId()) {
                        case R.id.action_movie_details:
                            position = 0;
                            break;
                        case R.id.action_movie_trailers:
                            position = 1;
                            break;
                        case R.id.action_movie_reviews:
                            position = 2;
                            break;

                        default:
                            position = -1;
                            break;
                    }

                    if(position != -1) {
                        mViewPager.setCurrentItem(position);
                        updateFragmentWithData(position);
                        return true;
                    }
                    return false;
        });

        mMovieViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        Intent intent = getIntent();
        if(intent!= null ) {
            if(intent.hasExtra(EXTRA_MOVIE_ID)){
                int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0);
                if (movieId > 0) {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mMovieViewModel.loadMovieDetails(movieId);
                }
            }

            if(intent.hasExtra(EXTRA_FAVORITE_DATA)) {
                mMovieDetails = intent.getParcelableExtra(EXTRA_FAVORITE_DATA);
                addedToFavorites = mMovieDetails != null;
            }
        }
    }

    private void updateFragmentWithData(int fragmentPosition) {

        UpdateFragmentListener fragment = (UpdateFragmentListener) mPagerAdapter.getFragments().get(fragmentPosition);
        if (fragment != null) {
            fragment.updateFragmentWithData(mMovieDetails);
        }
    }

    private void handleResponse(Movie movieDetails) {
        mLoadingIndicator.setVisibility(View.GONE);

        if(movieDetails != null) {
            mMovieDetails = movieDetails;
            updateFragmentWithData(mViewPager.getCurrentItem());
        }
        else if(mMovieDetails != null) {
            updateFragmentWithData(mViewPager.getCurrentItem());
        }
         else {
            Toast.makeText(this, getString(R.string.load_movies_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_favorites:
                if(!addedToFavorites) {
                    addMovieToFavorites();
                } else {
                    deleteMovieFromFavorites();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMovieFromFavorites(){
        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(mMovieDetails.getId());
        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        int rowsDeleted = getContentResolver().delete(uri, null, null);
        if(rowsDeleted > 0) {
            addedToFavorites = false;
            invalidateOptionsMenu();
        }
    }

    private void addMovieToFavorites(){
        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, mMovieDetails.getOriginalTitle());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_DURATION, mMovieDetails.getRuntime());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, mMovieDetails.getReleaseDate());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_POPULARITY, mMovieDetails.getPopularity());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RATING, mMovieDetails.getVoteAverage());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_GENRES, mMovieDetails.getGenres());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_STORYLINE, mMovieDetails.getOverview());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, mMovieDetails.getPosterPath());

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        if(uri != null) {
            addedToFavorites = true;
            invalidateOptionsMenu();
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favoritesMenu = menu.findItem(R.id.action_favorites);
        favoritesMenu.setIcon(addedToFavorites ? R.drawable.vector_favorite : R.drawable.vector_add_favorite);

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void loadMovieDetails() {
        mMovieViewModel.getMovieDetails().observe(this, this::handleResponse);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(EXTRA_FAVORITE_FLAG, addedToFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey(EXTRA_FAVORITE_FLAG)) {
            addedToFavorites = savedInstanceState.getBoolean(EXTRA_FAVORITE_FLAG);
            invalidateOptionsMenu();
        }
    }
}
