package com.popular.movies.details.trailers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.popular.movies.R;
import com.popular.movies.data.api.MoviesRepositoryImpl;
import com.popular.movies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private List<Trailer> mTrailers;
    private final TrailerAdapterOnClickListener mTrailerClickListener;

    TrailersAdapter(List<Trailer> trailers, TrailerAdapterOnClickListener listener) {
        mTrailers = trailers;
        mTrailerClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_trailer, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailers == null ? 0 : mTrailers.size();
    }

    public void setData(List<Trailer> trailers) {
        if(mTrailers != null) {
            mTrailers.clear();
        }
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public interface TrailerAdapterOnClickListener {
        void onItemClicked(Trailer trailer);
        void onShareClicked(Trailer trailer);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mTrailerThumbnail;
        private ImageView mShareButton;
        private RelativeLayout mLayoutMovieItem;
        private TextView mTrailerTitle;
        private Trailer mTrailer;

        TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerThumbnail = itemView.findViewById(R.id.trailer_thumbnail);
            mLayoutMovieItem = itemView.findViewById(R.id.trailer_layout);
            mShareButton = itemView.findViewById(R.id.trailer_share_icon);
            mTrailerTitle = itemView.findViewById(R.id.trailer_title);
            mLayoutMovieItem.setOnClickListener(this);
            mShareButton.setOnClickListener(this);
        }

        void bind(@NonNull Trailer trailer) {
            mTrailer = trailer;
            mTrailerTitle.setText(trailer.getName());
            String urlBuilder = MoviesRepositoryImpl.BASE_URL_TRAILER_THUMBNAIL.replace("video_key", trailer.getKey());
            Picasso.get()
                    .load(urlBuilder)
                    .error(R.drawable.vector_error)
                    .into(mTrailerThumbnail);
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if(viewId == R.id.trailer_layout) {
                mTrailerClickListener.onItemClicked(mTrailer);
            } else if (viewId == R.id.trailer_share_icon) {
                mTrailerClickListener.onShareClicked(mTrailer);
            }
        }
    }

}
