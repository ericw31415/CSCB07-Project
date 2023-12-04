package ca.utoronto.cscb07project.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String ARG_EVENT_ID = "eventId";

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

        if (getArguments() != null) {
            this.eventId = getArguments().getString(ARG_EVENT_ID);
        } else {
            Log.e("EventDetailsStudent", "Arguments are null");
            // Handle this case or return an empty view
            return view;
        }

        Button rsvpButton = view.findViewById(R.id.toRSVP);

        rsvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRSVP();
            }
        });

        if (eventId != null) {
            DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Events").child(eventId);

            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Event event = dataSnapshot.getValue(Event.class);

                        if (event != null) {
                            // Set text only if all values are non-null
                            eventTitle.setText(event.getTitle());
                            eventDateTime.setText(event.getDateTime());
                            eventLocation.setText(event.getLocation());
                            eventDescription.setText(event.getDescription());

                            // Check if the event is full and disable RSVP button
                            if (event.getParticipantCount() >= event.getParticipantLimit()) {
                                rsvpButton.setEnabled(false);
                                rsvpButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                                Toast.makeText(getActivity(), "Sorry, the event is full", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("EventDetailsStudent", "Event is null");
                        }
                    } else {
                        Log.d("EventDetailsStudent", "Event not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("EventDetailsStudent", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("EventDetailsStudent", "Event ID is null");
            // Handle this case or return an empty view
        }

        return view;
    }



    private void performRSVP() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            if (userEmail != null) {
                String eventId = getArguments().getString(ARG_EVENT_ID);

                if (eventId != null) {
                    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Events").child(eventId);

                    eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Event event = dataSnapshot.getValue(Event.class);

                                if (event != null) {
                                    // Check if the event is full
                                    if (event.getParticipantCount() >= event.getParticipantLimit()) {
                                        Log.d("RSVP", "The event is full and cannot accept more RSVPs");
                                        return;
                                    }

                                    // Add RSVP to the event
                                    boolean rsvpAdded = event.addRSVP(userEmail);

                                    if (rsvpAdded) {
                                        // Update the event in the database
                                        eventsRef.setValue(event);
                                        Log.d("RSVP", "RSVP added for Event ID: " + eventId);

                                        // Update UserEvents collection
                                        updateUserEventsCollection(eventId, userEmail);
                                    } else {
                                        Log.d("RSVP", "User has already RSVP'd for Event ID: " + eventId);
                                    }
                                } else {
                                    Log.e("RSVP", "Event is null");
                                }
                            } else {
                                Log.e("RSVP", "Event not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("RSVP", "Database error: " + databaseError.getMessage());
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
    private void updateUserEventsCollection(String eventId, String userEmail) {
        // Encode the email address to create a valid Firebase Database path
        String encodedEmail = encodeEmail(userEmail);

        DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference("UserEvents").child(encodedEmail);

        userEventsRef.child(eventId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("UserEvents", "Event ID added to UserEvents collection");
                    } else {
                        Log.e("UserEvents", "Failed to add Event ID to UserEvents collection");
                    }
                });
    }
    private String encodeEmail(String email) {
        // Replace forbidden characters with valid ones for Firebase Database paths
        return email.replace(".", ",");
    }


    // ...

    /*private void addRSVPToDatabase(String eventId, String userEmail) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Events").child(eventId);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        boolean rsvpAdded = event.addRSVP(userEmail);
                        if (rsvpAdded) {
                            eventRef.setValue(event);
                            Log.d("RSVP", "RSVP added for Event ID: " + eventId);
                        } else {
                            Log.d("RSVP", "The event is full and cannot accept more RSVPs");
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
                Log.e("RSVP", "Database error: " + databaseError.getMessage());
            }
        });
    }
    */


// ...



}
