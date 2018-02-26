package com.popular.movies.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.popular.movies.R;
import com.popular.movies.details.MovieDetailsActivity;
import com.popular.movies.model.Movie;
import com.popular.movies.model.MoviesWrapper;
import com.popular.movies.model.MoviesWrapperViewModel;

public class MoviesOverviewActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickListener{
    private static final String EXTRA_SORTING_CRITERIA = "sort_criteria";
    private static final String SORT_BY_POPULARITY_CRITERIA = "popular";
    private static final String SORT_BY_TOP_RATINGS_CRITERIA = "top_rated";
    MoviesWrapperViewModel mMoviesViewModel;
    MoviesAdapter mMoviesAdapter;
    String selectedSortCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_overview);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_movies);
        int columns = getResources().getInteger(R.integer.columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columns, spacingInPixels, true, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mMoviesAdapter = new MoviesAdapter(null, this);
        recyclerView.setAdapter(mMoviesAdapter);

        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesWrapperViewModel.class);
        // Handle changes emitted by LiveData
        mMoviesViewModel.getMoviesWrapper().observe(this, moviesWrapper -> handleResponse(moviesWrapper));
        applySorting(SORT_BY_TOP_RATINGS_CRITERIA);
    }

    private void handleResponse(MoviesWrapper moviesWrapper) {
        if(moviesWrapper == null) {
            Toast.makeText(this, getString(R.string.load_movies_error), Toast.LENGTH_SHORT).show();
            return;
        }
        mMoviesAdapter.setData(moviesWrapper.getResults());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem sortByPopularityItem = menu.findItem(R.id.action_sort_popular);
        MenuItem sortByRatingsItem = menu.findItem(R.id.action_sort_top_rated);
        if(SORT_BY_POPULARITY_CRITERIA.equals(selectedSortCriteria)) {
            sortByPopularityItem.setChecked(true);
        }
        else {
            sortByRatingsItem.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_top_rated:
                if(!item.isChecked()) {
                    applySorting(SORT_BY_TOP_RATINGS_CRITERIA);
                    item.setChecked(true);
                }
                return true;

            case R.id.action_sort_popular:
                if(!item.isChecked()) {
                    applySorting(SORT_BY_POPULARITY_CRITERIA);
                    item.setChecked(true);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void applySorting(String sortCriteria) {
        selectedSortCriteria = sortCriteria;
        mMoviesViewModel.loadMovies(selectedSortCriteria);
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

    @Override
    public void onItemClicked(View transitionView, Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        transitionView,
                        ViewCompat.getTransitionName(transitionView));
        startActivity(intent, options.toBundle());
    }
}
