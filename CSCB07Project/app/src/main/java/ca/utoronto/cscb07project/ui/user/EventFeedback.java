package ca.utoronto.cscb07project.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        String eventId = getArguments() != null ? getArguments().getString("eventId") : null;

        if (recyclerView != null) {
            adapter = new FeedbackAdapter(getContext(), R.layout.review_item, reviews);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new FeedbackAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Review review) {
                    openFeedbackFragment(review);
                }
            });

            DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("Reviews");

            if (eventId != null) {
                fetchFeedbackFromFirebase(reviewsRef, eventId);
            }
        }
    }

    private void fetchFeedbackFromFirebase(DatabaseReference reviewsRef, String eventId) {
        reviewsRef.orderByChild("eventId").equalTo(eventId).addValueEventListener(new ValueEventListener() {
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
    }
}
