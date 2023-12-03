package ca.utoronto.cscb07project.ui.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.announcements.Announcement;
import ca.utoronto.cscb07project.events.Event;
import ca.utoronto.cscb07project.events.EventAdapter;

public class AddAnnouncmentFragment extends Fragment {

    private EditText titleEditText;
    private EditText descriptionEditText;

    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventsList;

    private DatabaseReference eventsRef;

    private CheckBox sendToAllCheckBox;
    private Announcement announcement;

    private String EventID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_announcment, container, false);

        titleEditText = view.findViewById(R.id.AnnouncementTitleEditText);
        descriptionEditText = view.findViewById(R.id.AnnouncementDescriptionEditText);
        Button submitButton = view.findViewById(R.id.postAnnouncementButton);
        sendToAllCheckBox = view.findViewById(R.id.sendToAllCheckBox);

        eventsRecyclerView = view.findViewById(R.id.recyclerViewEvent);
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(requireContext(), R.layout.event_item, eventsList);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventsRecyclerView.setAdapter(eventAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        eventsRef = database.getReference("Events");
        fetchEventsFromFirebase();

        submitButton.setOnClickListener(v -> postAnnouncement());

        sendToAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                eventsRecyclerView.setVisibility(View.VISIBLE);
                // Clear Event ID when checkbox is checked
                if (announcement != null) {
                    announcement.setEventID("");
                }
            } else {
                eventsRecyclerView.setVisibility(View.GONE);
            }
        });

        eventAdapter.setOnItemClickListener(event -> {
            if (sendToAllCheckBox.isChecked()) {
                Toast.makeText(requireContext(), "EventId: " + event.getId(), Toast.LENGTH_SHORT).show();
                EventID = event.getId();
            } else {
                Toast.makeText(requireContext(), "Checkbox is unchecked", Toast.LENGTH_SHORT).show();
                EventID = "";
            }
        });



        return view;
    }

    private void fetchEventsFromFirebase() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsList.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    eventsList.add(event);
                }

                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void setEventIdInAnnouncement(Event event) {
        if (event != null) {
            // Set the event ID in the announcement
            announcement.setEventID(event.getId());
        }
    }

    private void postAnnouncement() {
        String title = titleEditText.getText().toString().trim();
        String details = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference announcementsRef = database.getReference("Announcements");
        String AnnouncementId = announcementsRef.push().getKey();

        if (AnnouncementId != null) {
            announcement = new Announcement(title, formattedDate, details, AnnouncementId, EventID);

            if (sendToAllCheckBox.isChecked()) {
                // If checkbox is checked, set event ID to blank
                announcement.setEventID(EventID);
            }else{
                announcement.setEventID("");
            }

            announcementsRef.child(AnnouncementId).setValue(announcement)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Announcement posted", Toast.LENGTH_SHORT).show();
                            sendPushNotification(title, details);
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Post failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void sendPushNotification(String title, String details) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel_id",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default_channel_id")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(details)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
