package ca.utoronto.cscb07project.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;

public class EventDetailsStudent extends Fragment {

    private static final String ARG_EVENT_ID = "event_id";

    private String eventId;
    private TextView eventTitle;
    private TextView eventDateTime;
    private TextView eventLocation;
    private TextView eventDescription;

    public EventDetailsStudent() {
        // Required empty public constructor
    }

    public static EventDetailsStudent newInstance(String eventId) {
        EventDetailsStudent fragment = new EventDetailsStudent();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details_student, container, false);

        eventTitle = view.findViewById(R.id.eventTitleUser);
        eventDateTime = view.findViewById(R.id.eventDateUser);
        eventLocation = view.findViewById(R.id.eventLocationUser);
        eventDescription = view.findViewById(R.id.eventDescUser);
        this.eventId = getArguments().getString("eventID");

        Button rsvpButton = view.findViewById(R.id.toRSVP); // Assuming the button ID is "rsvpButton"

        rsvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle RSVP button click
                performRSVP();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String eventId = getArguments().getString("eventId");
        DatabaseReference eventsRef = database.getReference("Events").child(eventId);
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String dateTime = dataSnapshot.child("dateTime").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);

                    eventTitle.setText(title);
                    eventDateTime.setText(dateTime);
                    eventLocation.setText(location);
                    eventDescription.setText(description);
                } else {
                    Log.d("Why", "Not working");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        return view;
    }

    private void performRSVP() {
        // Get the current user from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Get the current user's email
            String userEmail = currentUser.getEmail();

            if (userEmail != null) {
                String eventId = getArguments().getString("eventId");

                if (eventId != null) {
                    // Check if the RSVP entry already exists
                    DatabaseReference rsvpsRef = FirebaseDatabase.getInstance().getReference("RSVPS");
                    Query query = rsvpsRef.orderByChild("eventID").equalTo(eventId);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean hasExistingRSVP = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                RSVP rsvp = snapshot.getValue(RSVP.class);

                                if (rsvp != null && rsvp.getEmail().equalsIgnoreCase(userEmail)) {
                                    // Entry already exists, handle accordingly
                                    Log.d("RSVP", "RSVP already exists for Event ID: " + eventId);
                                    hasExistingRSVP = true;
                                    break;
                                }
                            }

                            if (!hasExistingRSVP) {
                                // Entry does not exist, add a new RSVP
                                addRSVPToDatabase(eventId, userEmail);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                } else {
                    Log.e("RSVP", "Event ID is null");
                }
            } else {
                Log.e("RSVP", "User email is null");
            }
        } else {
            Log.e("RSVP", "No user logged in");
        }
    }

    // ...

    private void addRSVPToDatabase(String eventId, String userEmail) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Events").child(eventId);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        // Add the RSVP to the event
                        boolean rsvpAdded = event.addRSVP(userEmail);
                        if (rsvpAdded) {
                            // Update the event in the database
                            eventRef.setValue(event);
                            // Add your logic to handle successful RSVP
                            Log.d("RSVP", "RSVP added for Event ID: " + eventId);
                        } else {
                            Log.d("RSVP", "The event is full and cannot accept more RSVPs");
                            // Add your logic to handle this case (e.g., show a message)
                        }
                    } else {
                        Log.e("RSVP", "Event is null");
                    }
                } else {
                    Log.e("RSVP", "Event does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

// ...



}
