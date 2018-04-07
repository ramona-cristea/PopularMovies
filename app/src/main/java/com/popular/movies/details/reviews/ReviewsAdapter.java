package com.popular.movies.details.reviews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popular.movies.R;
import com.popular.movies.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Review> mReviews;

    ReviewsAdapter(List<Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_review, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.size();
    }

    public void setData(List<Review> reviews) {
        if(mReviews != null) {
            mReviews.clear();
        }
        mReviews = reviews;
        notifyDataSetChanged();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mReviewAuthor;
        private TextView mReviewContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewAuthor = itemView.findViewById(R.id.text_review_author);
            mReviewContent = itemView.findViewById(R.id.text_review_content);
        }

        void bind(@NonNull Review review) {
            mReviewAuthor.setText(review.getAuthor());
            mReviewContent.setText(review.getContent());
        }

        @Override
        public void onClick(View view) {
        }
    }

}
