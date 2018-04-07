package com.popular.movies.details.trailers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popular.movies.R;
import com.popular.movies.details.UpdateFragmentListener;
import com.popular.movies.model.Movie;
import com.popular.movies.model.Trailer;

import java.util.ArrayList;
import java.util.List;


public class TrailersFragment extends Fragment implements UpdateFragmentListener, TrailersAdapter.TrailerAdapterOnClickListener {

    TrailersAdapter mTrailersAdapter;

    public static TrailersFragment newInstance (){
        return new TrailersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mTrailersAdapter = new TrailersAdapter(null, this);
        recyclerView.setAdapter(mTrailersAdapter);
        return view;
    }

    @Override
    public void updateFragmentWithData(Object movie) {
        if(movie == null) {
            return;
        }
        Movie movieDetails = (Movie) movie;
        if(movieDetails.getVideos() != null && movieDetails.getVideos().getResults() != null) {
            List<Trailer> trailerList = new ArrayList<>();
            for(Trailer video : movieDetails.getVideos().getResults()) {
                if(video.getType().equals("Trailer")) {
                    trailerList.add(video);
                }
            }
            mTrailersAdapter.setData(trailerList);
        }
    }

    @Override
    public void onItemClicked(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));

        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onShareClicked(Trailer trailer) {
        ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_trailer_text))
                .setText("http://www.youtube.com/watch?v=" + trailer.getKey())
                .startChooser();
    }
}
