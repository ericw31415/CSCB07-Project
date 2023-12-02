package ca.utoronto.cscb07project.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsStudent#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventId ID of the event to display details for.
     * @return A new instance of fragment EventDetailsStudent.
     */
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

        // Retrieve event details from Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String eventId = getArguments().getString("eventId");
        Log.d("", eventId);
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

}
