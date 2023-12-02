package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.events.Event;
import ca.utoronto.cscb07project.events.FeedbackAdapter;
import ca.utoronto.cscb07project.events.Review;

public class EventFeedback extends Fragment {

    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private List<Review> reviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_feedback, container, false);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        reviews = new ArrayList<>();

        // Create FeedbackAdapter and set it to the RecyclerView
        adapter = new FeedbackAdapter(getContext(), R.layout.review_item, reviews);

        // The following line is the source of the NullPointerException
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FeedbackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Review review) {
                openFeedbackFragment(review);
            }
        });

        // Initialize Realtime Database reference
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("Reviews");

        // Replace the child("exampleEventId") with the actual event ID you want to fetch reviews for
        fetchFeedbackFromFirebase(reviewsRef.child("exampleEventId"));
    }


    private void fetchFeedbackFromFirebase(DatabaseReference reviewsRef) {
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviews.clear();

                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    reviews.add(review);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void openFeedbackFragment(Review review) {
        // Handle the action when a review is clicked, e.g., navigate to a detailed view
    }
}
