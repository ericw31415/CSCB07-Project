package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.events.Event;
import ca.utoronto.cscb07project.events.Review;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFeedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFeedback extends Fragment {

    private ListView listView;
    private ArrayAdapter<Review> adapter;
    private List<Review> reviews;
    private List<Event> events;
    private DatabaseReference reviewRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_feedback, container, false);
        setupListView(view);
        return view;
    }

    private void setupListView(View view) {
        listView = view.findViewById(R.id.listView500);
        reviews = new ArrayList<>();

        //create review_item layout
        adapter = new ArrayAdapter<Review>(getContext(), R.layout.review_item, reviews) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View itemView = convertView;
                if (itemView == null) {
                    itemView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
                }

                Review review = getItem(position);
                if (review != null) {
                    TextView eventId = itemView.findViewById(R.id.eventId);
                    TextView rating = itemView.findViewById(R.id.rating);
                    TextView reviewText = itemView.findViewById(R.id.reviewText);
                    TextView userEmail = itemView.findViewById(R.id.userEmail);


                    eventId.setText(review.getEventId());
                    rating.setText(review.getRating());
                    reviewText.setText(review.getReviewText());
                    userEmail.setText(review.getUserEmail());

                }

                return itemView;
            }
        };

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click, e.g., open detail fragment
                //Review review = reviews.get(position);
                //openFeedbackFragment(review);
                /**
                Event selectedEvent = events.get(position);

                String eventId = selectedEvent.getId();

                fetchFeedbackFromFirebase(eventId);
                 */
                Review selectedReview = reviews.get(position);
                openFeedbackFragment(selectedReview);
            }
        });


        // Initialize Realtime Database reference
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //reviewRef = database.getReference("Reviews");

        // Fetch events from Realtime Database
        //DatabaseReference eventRef = database.getReference("Events").child();
        //fetchFeedbackFromFirebase(eventRef);
    }

    private void fetchFeedbackFromFirebase(String eventId) {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("Reviews").child(eventId);
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
        /**
        EventFeedback feedback = new EventFeedback();
        Bundle args = new Bundle();
        args.putString("eventId", review.getEventId());
        feedback.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, feedback);
        transaction.addToBackStack(null);
        transaction.commit();
         */
    }
}