package com.popular.movies.overview;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.popular.movies.R;
import com.popular.movies.api.MoviesRepositoryImpl;
import com.popular.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> mMovies;
    private final MoviesAdapterOnClickListener mMovieClickListener;

    MoviesAdapter(List<Movie> movies, MoviesAdapterOnClickListener listener) {
        mMovies = movies;
        mMovieClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie temperatureData = mMovies.get(position);
        holder.bind(temperatureData);
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public void setData(List<Movie> movies) {
        if(mMovies != null) {
            mMovies.clear();
        }
        mMovies = movies;
        notifyDataSetChanged();
    }

    public interface MoviesAdapterOnClickListener {
        void onItemClicked(View transitionView, Movie movie);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageMoviePoster;
        private LinearLayout layoutMovieItem;
        private Movie mMovie;
        MovieViewHolder(View itemView) {
            super(itemView);
            imageMoviePoster = itemView.findViewById(R.id.image_movie_poster);
            layoutMovieItem = itemView.findViewById(R.id.layout_movie_item);
            layoutMovieItem.setOnClickListener(this);
        }

        void bind(@NonNull Movie movie) {
            mMovie = movie;
            String urlBuilder = MoviesRepositoryImpl.BASE_URL_IMAGES +
                    movie.getPosterPath();
            Resources res = imageMoviePoster.getResources();
            Picasso.with(imageMoviePoster.getContext())
                    .load(urlBuilder)
                    .resize(res.getDimensionPixelSize(R.dimen.poster_width),
                            res.getDimensionPixelSize(R.dimen.poster_height))
                    .placeholder(R.drawable.vector_placeholder_poster)
                    .error(R.drawable.vector_error)
                    .centerCrop()
                    .into(imageMoviePoster);
        }

        @Override
        public void onClick(View view) {
            mMovieClickListener.onItemClicked(imageMoviePoster, mMovie);
        }
    }

}
