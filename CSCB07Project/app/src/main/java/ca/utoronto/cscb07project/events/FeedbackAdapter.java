package ca.utoronto.cscb07project.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.utoronto.cscb07project.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.EventViewHolder> {

    private List<Review> reviews;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Review review);
    }

    public FeedbackAdapter(Context context, int event_item, List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Review currentRev = reviews.get(position);
        holder.bind(currentRev, listener);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        private TextView userEmail;
        private TextView rating;
        private TextView reviewText;

        private TextView eventId;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmail);
            rating = itemView.findViewById(R.id.rating);
            reviewText = itemView.findViewById(R.id.reviewText);
            eventId = itemView.findViewById(R.id.eventId);
        }

        public void bind(final Review review, final OnItemClickListener listener) {
            userEmail.setText(review.getUserEmail());
            rating.setText(String.valueOf(review.getRating())); // Convert rating to String
            reviewText.setText(review.getReviewText());
            eventId.setText(review.getEventId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(review);
                    }
                }
            });
        }
    }
}
