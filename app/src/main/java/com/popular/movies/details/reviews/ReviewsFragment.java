package com.popular.movies.details.reviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popular.movies.R;
import com.popular.movies.details.UpdateFragmentListener;
import com.popular.movies.model.Movie;

public class ReviewsFragment extends Fragment implements UpdateFragmentListener {

    ReviewsAdapter mReviewsAdapter;

    public static ReviewsFragment newInstance (){
        return new ReviewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mReviewsAdapter = new ReviewsAdapter(null);
        recyclerView.setAdapter(mReviewsAdapter);
        return view;
    }

    @Override
    public void updateFragmentWithData(Object movie) {
        if(movie == null) {
            return;
        }
        Movie movieDetails = (Movie) movie;
        if(movieDetails.getReviews() != null && movieDetails.getReviews().getResults() != null) {
            mReviewsAdapter.setData(movieDetails.getReviews().getResults());
        }
    }
}
