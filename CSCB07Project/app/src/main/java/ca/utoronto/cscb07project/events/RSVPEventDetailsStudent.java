package ca.utoronto.cscb07project.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;

public class RSVPEventDetailsStudent extends Fragment {

    private static final String ARG_EVENT_ID = "event_id";

    private String eventId;
    private TextView eventTitle;
    private TextView eventDateTime;
    private TextView eventLocation;
    private TextView eventDescription;

    private FirebaseAuth auth;

    public RSVPEventDetailsStudent() {
    }

    public static RSVPEventDetailsStudent newInstance(String eventId) {
        RSVPEventDetailsStudent fragment = new RSVPEventDetailsStudent();
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
        View view = inflater.inflate(R.layout.fragment_r_s_v_p_event_details_student, container, false);

        eventTitle = view.findViewById(R.id.eventTitleUser);
        eventDateTime = view.findViewById(R.id.eventDateUser);
        eventLocation = view.findViewById(R.id.eventLocationUser);
        eventDescription = view.findViewById(R.id.eventDescUser);

        Button leaveReviewButton = view.findViewById(R.id.leaveReviewButton);

        leaveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                    Toast.makeText(getContext(), "Event not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });



        return view;
    }

    private void showReviewDialog() {
        if (eventId != null) {
            LeaveReviewDialogFragment dialog = LeaveReviewDialogFragment.newInstance(eventId);
            dialog.show(getChildFragmentManager(), "LeaveReviewDialogFragment");
        }
    }
}
