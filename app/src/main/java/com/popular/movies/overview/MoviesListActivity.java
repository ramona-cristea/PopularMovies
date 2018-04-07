package com.popular.movies.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.popular.movies.R;
import com.popular.movies.data.database.FavoriteContract;
import com.popular.movies.details.MovieDetailsActivity;
import com.popular.movies.model.DataWrapper;
import com.popular.movies.model.Movie;
import com.popular.movies.model.MoviesWrapperViewModel;

import java.util.ArrayList;

public class MoviesListActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterHandler,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String EXTRA_SORTING_CRITERIA = "sort_criteria";
    private static final String SORT_BY_POPULARITY_CRITERIA = "popular";
    private static final String SORT_BY_TOP_RATINGS_CRITERIA = "top_rated";
    private static final String SORT_BY_FAVORITES = "favorites";
    MoviesWrapperViewModel mMoviesViewModel;
    MoviesAdapter mMoviesAdapter;
    String selectedSortCriteria = SORT_BY_TOP_RATINGS_CRITERIA;
    ProgressBar mLoadingIndicator;

    ArrayList<Movie> mFavoriteMovies;

    /*
    * The columns of data needed in order to display Favorite Movies
    */
    public static final String[] FAVORITE_PROJECTION = {
            FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID,
            FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,
            FavoriteContract.FavoriteEntry.COLUMN_DURATION,
            FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE,
            FavoriteContract.FavoriteEntry.COLUMN_POPULARITY,
            FavoriteContract.FavoriteEntry.COLUMN_RATING,
            FavoriteContract.FavoriteEntry.COLUMN_GENRES,
            FavoriteContract.FavoriteEntry.COLUMN_STORYLINE,
            FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH
    };

    public static final int INDEX_FAVORITE_ID = 0;
    public static final int INDEX_FAVORITE_ORIGINAL_TITLE = 1;
    public static final int INDEX_FAVORITE_DURATION = 2;
    public static final int INDEX_FAVORITE_RELEASE_DATE = 3;
    public static final int INDEX_FAVORITE_POPULARITY = 4;
    public static final int INDEX_FAVORITE_RATING = 5;
    public static final int INDEX_FAVORITE_GENRES = 6;
    public static final int INDEX_FAVORITE_STORYLINE = 7;
    public static final int INDEX_FAVORITE_POSTER_PATH = 8;

    /*
     * Loader ID responsible for loading our favorite movies.
     */
    private static final int ID_FAVORITES_LOADER = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_overview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movies_overview);
        }

        mLoadingIndicator = findViewById(R.id.progress_bar_details);
        RecyclerView recyclerView = findViewById(R.id.recycler_movies_overview);
        int columns = getResources().getInteger(R.integer.columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columns, spacingInPixels, true, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mMoviesAdapter = new MoviesAdapter(null, this);
        recyclerView.setAdapter(mMoviesAdapter);

        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesWrapperViewModel.class);
        // Handle changes emitted by LiveData
        mMoviesViewModel.getMoviesWrapper().observe(this, this::handleResponse);
        getSupportLoaderManager().restartLoader(ID_FAVORITES_LOADER, null, this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_top_rated:
                            applySorting(SORT_BY_TOP_RATINGS_CRITERIA);
                            break;
                        case R.id.action_most_popular:
                            applySorting(SORT_BY_POPULARITY_CRITERIA);
                            break;
                        case R.id.action_favorites:
                            applySorting(SORT_BY_FAVORITES);
                            break;

                        default:
                            break;
                    }

                    return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySorting(selectedSortCriteria);
    }

    private void applySorting(String sortCriteria) {
        selectedSortCriteria = sortCriteria;
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if(sortCriteria.equals(SORT_BY_FAVORITES)) {
            getSupportLoaderManager().restartLoader(ID_FAVORITES_LOADER, null, this);
        } else {
            mMoviesViewModel.loadMovies(selectedSortCriteria);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(EXTRA_SORTING_CRITERIA, selectedSortCriteria);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey(EXTRA_SORTING_CRITERIA)) {
            applySorting(savedInstanceState.getString(EXTRA_SORTING_CRITERIA));
            invalidateOptionsMenu();
        }
    }

    private void handleResponse(DataWrapper<Movie> moviesWrapper) {
        mLoadingIndicator.setVisibility(View.GONE);
        if(moviesWrapper == null) {
            Toast.makeText(this, getString(R.string.load_movies_error), Toast.LENGTH_SHORT).show();
            return;
        }
        mMoviesAdapter.setData(moviesWrapper.getResults());
    }

    @Override
    public void onItemClicked( Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movie.getId());
        intent.putExtra(MovieDetailsActivity.EXTRA_FAVORITE_DATA, getFavorite(movie.getId()));
        startActivity(intent);
    }

    public Movie getFavorite(int movieId) {

        if(mFavoriteMovies != null) {
            for(Movie movie : mFavoriteMovies) {
                if(movieId == movie.getId()) {
                    return movie;
                }
            }
        }
        return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_FAVORITES_LOADER:
                Uri favoritesQueryUri = FavoriteContract.FavoriteEntry.CONTENT_URI;

                return new CursorLoader(this,
                        favoritesQueryUri,
                        FAVORITE_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            setMoviesFromCursor(data);
            if(selectedSortCriteria.equals(SORT_BY_FAVORITES)) {
                mLoadingIndicator.setVisibility(View.GONE);
                mMoviesAdapter.setData(mFavoriteMovies);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setMoviesFromCursor(Cursor cursor){
        if(mFavoriteMovies == null) {
            mFavoriteMovies = new ArrayList<>();
        }
        else {
            mFavoriteMovies.clear();
        }

        while(cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(INDEX_FAVORITE_ID));
            movie.setOriginalTitle(cursor.getString(INDEX_FAVORITE_ORIGINAL_TITLE));
            movie.setRuntime(cursor.getInt(INDEX_FAVORITE_DURATION));
            movie.setReleaseDate(cursor.getString(INDEX_FAVORITE_RELEASE_DATE));
            movie.setPopularity(cursor.getDouble(INDEX_FAVORITE_POPULARITY));
            movie.setVoteAverage(cursor.getDouble(INDEX_FAVORITE_RATING));
            movie.setGenresAsString(cursor.getString(INDEX_FAVORITE_GENRES));
            movie.setOverview(cursor.getString(INDEX_FAVORITE_STORYLINE));
            movie.setPosterPath(cursor.getString(INDEX_FAVORITE_POSTER_PATH));
            mFavoriteMovies.add(movie);
        }
    }
}
