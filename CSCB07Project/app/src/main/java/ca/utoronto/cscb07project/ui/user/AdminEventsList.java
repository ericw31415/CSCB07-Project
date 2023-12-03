package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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

public class AdminEventsList extends Fragment{

    private ListView listView;
    private ArrayAdapter<Event> adapter;
    private List<Event> events;
    private DatabaseReference eventsRef;

    //private TextView revCountTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        setupListView(view);
        return view;
    }

    private void setupListView(View view) {
        listView = view.findViewById(R.id.listView500);
        events = new ArrayList<>();

        adapter = new ArrayAdapter<Event>(getContext(), R.layout.admin_event_item, events) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View itemView = convertView;
                if (itemView == null) {
                    itemView = LayoutInflater.from(getContext()).inflate(R.layout.admin_event_item, parent, false);
                }

                Event event = getItem(position);
                if (event != null) {
                    TextView titleTextView = itemView.findViewById(R.id.title);
                    TextView locationTextView = itemView.findViewById(R.id.location);
                    TextView dateTimeTextView = itemView.findViewById(R.id.dateTime);

                    TextView revCountTextView = itemView.findViewById(R.id.revCount);
                    TextView avgRatingTextView = itemView.findViewById(R.id.avgRating);

                    titleTextView.setText(event.getTitle());
                    locationTextView.setText(event.getLocation());
                    dateTimeTextView.setText(event.getDateTime());
                    //revCountTextView.setText();
                    //avgRatingTextView.setText();

                    // Now, fetch and display the reviews count for each event
                    countReviewsForEvent(event.getId(), new CountReviewsCallback() {
                        @Override
                        public void onCountReceived(long reviewsCount, double averageRating) {
                            //revCountTextView.setText("Number of Reviews: " + reviewsCount);
                            if (revCountTextView != null && revCountTextView.getParent() != null) {
                                revCountTextView.setText("Number of Reviews: " + reviewsCount);
                                avgRatingTextView.setText("Average Review Rating: " + averageRating);
                            }
                        }

                        @Override
                        public void onCountError(String error) {
                            Log.e("AdminEventsList", "Error counting reviews: " + error);
                        }
                    });
                }

                return itemView;
            }
        };

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click, e.g., open detail fragment
                Event event = events.get(position);
                openFeedbackFragment(event);
            }
        });


        // Initialize Realtime Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        eventsRef = database.getReference("Events");

        // Fetch events from Realtime Database
        fetchEventsFromFirebase();
    }

    private void fetchEventsFromFirebase() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    events.add(event);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void openFeedbackFragment(Event event) {
        EventFeedback feedback = new EventFeedback();
        Bundle args = new Bundle();
        args.putString("eventId", event.getId());
        feedback.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, feedback);
        transaction.addToBackStack(null);
        transaction.commit();
    }




    private void countReviewsForEvent(String eventId, final CountReviewsCallback callback) {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("Reviews");

        reviewsRef.orderByChild("eventId").equalTo(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long reviewsCount = dataSnapshot.getChildrenCount();

                double totalRating = 0;

                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    // Assuming there's a child named "rating" under each review
                    double rating = reviewSnapshot.child("rating").getValue(Double.class);
                    totalRating += rating;
                }

                double averageRating = (reviewsCount > 0) ? totalRating / reviewsCount : 0;

                callback.onCountReceived(reviewsCount, averageRating);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                callback.onCountError(databaseError.getMessage());
            }
        });
    }
}