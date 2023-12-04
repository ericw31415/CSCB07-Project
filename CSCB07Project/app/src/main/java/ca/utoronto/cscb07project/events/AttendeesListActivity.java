package ca.utoronto.cscb07project.events;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;

public class AttendeesListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> events;
    private DatabaseReference eventsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list);

        listView = findViewById(R.id.attendeesListView);
        events = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        listView.setAdapter(adapter);

        // Initialize Firebase Database reference
        eventsRef = FirebaseDatabase.getInstance().getReference("Events");

        // Retrieve events from Firebase
        retrieveEventsFromFirebase();

        // Set item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventDetails = events.get(position);
                String eventId = getEventIdFromDetails(eventDetails);
                if (eventId != null) {
                    showRSVPDialog(eventId);
                }
            }
        });
    }

    private void retrieveEventsFromFirebase() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        String title = event.getTitle();
                        String dateTime = event.getDateTime();
                        String location = event.getLocation();
                        String eventId = eventSnapshot.getKey();

                        String eventDetails = "Title: " + title + "\n" +
                                "Date & Time: " + dateTime + "\n" +
                                "Location: " + location + "\n" +
                                "Event ID: " + eventId;

                        events.add(eventDetails);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private String getEventIdFromDetails(String eventDetails) {
        int startIndex = eventDetails.lastIndexOf("Event ID: ") + 10;
        int endIndex = eventDetails.length();
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return eventDetails.substring(startIndex, endIndex);
        }
        return null;
    }

    private void showRSVPDialog(String eventId) {
        DatabaseReference rsvpsRef = eventsRef.child(eventId).child("rsvps");
        rsvpsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> rsvps = new ArrayList<>();

                for (DataSnapshot rsvpSnapshot : dataSnapshot.getChildren()) {
                    String rsvp = rsvpSnapshot.getValue(String.class);
                    if (rsvp != null) {
                        rsvps.add(rsvp);
                    }
                }

                if (!rsvps.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendeesListActivity.this);
                    builder.setTitle("RSVPs for Event");
                    builder.setItems(rsvps.toArray(new String[0]), null);
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(AttendeesListActivity.this, "No RSVPs for this event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}