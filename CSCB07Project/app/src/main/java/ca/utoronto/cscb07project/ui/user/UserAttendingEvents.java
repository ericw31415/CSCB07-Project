package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.events.Event;
import ca.utoronto.cscb07project.events.RSVPEventAdapter;
import ca.utoronto.cscb07project.events.RSVPEventAdapter.OnItemClickListener;
import ca.utoronto.cscb07project.events.RSVPEventDetailsStudent;

public class UserAttendingEvents extends Fragment {

    private RecyclerView recyclerView;
    private RSVPEventAdapter adapter;
    private List<Event> rsvpEvents;
    private DatabaseReference eventsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_attending_events, container, false);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        rsvpEvents = new ArrayList<>();
        adapter = new RSVPEventAdapter(getContext(), rsvpEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                openRSVPDetailFragment(event);
            }
        });

        // Initialize Realtime Database reference
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            eventsRef = database.getReference("Events");

            // Fetch RSVP'd events from Realtime Database
            fetchRSVPEventsFromFirebase(currentUser.getEmail());
        }
    }

    private void fetchRSVPEventsFromFirebase(String userEmail) {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rsvpEvents.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);

                    // Check if the current user's email is in the RSVP list
                    if (event != null && event.getRsvps() != null && event.getRsvps().contains(userEmail)) {
                        rsvpEvents.add(event);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void openRSVPDetailFragment(Event event) {
        RSVPEventDetailsStudent rsvpEventDetailsStudent = new RSVPEventDetailsStudent();
        Bundle args = new Bundle();
        args.putString("event_id", event.getId());
        rsvpEventDetailsStudent.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, rsvpEventDetailsStudent);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
