package ca.utoronto.cscb07project.ui.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private String eventTopic;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcment, container, false);

        // Initialize UI elements
        initUIElements(view);

        // Fetch events from Firebase
        fetchEventsFromFirebase();

        // Handle checkbox changes
        handleCheckboxChanges();

        return view;
    }

    private void initUIElements(View view) {
        titleEditText = view.findViewById(R.id.AnnouncementTitleEditText);
        descriptionEditText = view.findViewById(R.id.AnnouncementDescriptionEditText);
        Button submitButton = view.findViewById(R.id.postAnnouncementButton);
        sendToAllCheckBox = view.findViewById(R.id.sendToAllCheckBox);

        eventsRecyclerView = view.findViewById(R.id.recyclerViewEvent);
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(requireContext(), R.layout.event_item, eventsList);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventsRecyclerView.setAdapter(eventAdapter);

        submitButton.setOnClickListener(v -> postAnnouncement());
    }

    private void fetchEventsFromFirebase() {
        eventsRef = FirebaseDatabase.getInstance().getReference("Events");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsList.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventsList.add(event);
                    }
                }

                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void handleCheckboxChanges() {
        sendToAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                eventsRecyclerView.setVisibility(View.VISIBLE);
                // Clear Event Topic when the checkbox is checked
                eventTopic = null;
            } else {
                eventsRecyclerView.setVisibility(View.GONE);
            }
        });

        eventAdapter.setOnItemClickListener(event -> {
            if (sendToAllCheckBox.isChecked()) {
                Toast.makeText(requireContext(), "EventId: " + event.getId(), Toast.LENGTH_SHORT).show();
                eventTopic = event.getId();
            } else {
                Toast.makeText(requireContext(), "Checkbox is unchecked", Toast.LENGTH_SHORT).show();
                eventTopic = null;
            }
        });
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

        DatabaseReference announcementsRef = FirebaseDatabase.getInstance().getReference("Announcements");
        String announcementId = announcementsRef.push().getKey();

        if (announcementId != null) {
            announcement = new Announcement(title, formattedDate, details, announcementId, eventTopic);

            if (sendToAllCheckBox.isChecked()) {
                announcement.setEventID(eventTopic);
            } else {
                announcement.setEventID("blank");
            }

            announcementsRef.child(announcementId).setValue(announcement)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Announcement posted", Toast.LENGTH_SHORT).show();
                            handleAnnouncementPostSuccess();
                        } else {
                            Toast.makeText(getContext(), "Post failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void handleAnnouncementPostSuccess() {
        if (!("blank".equals(eventTopic))) {
            // Specific event case
            Log.d("Test", "Specific");
            retrieveAndSendNotificationsForEvent();
        } else {
            // Default case
            Log.d("Default", "Announcement");
            retrieveAndSendNotificationsForAllUsers();
        }

        // Common actions after posting announcement
        sendPushNotification(announcement.getTitle(), announcement.getDescription());
        getParentFragmentManager().popBackStack();
    }

    private void retrieveAndSendNotificationsForEvent() {
        if (eventTopic != null) {
            DatabaseReference eventParticipantsRef = FirebaseDatabase.getInstance().getReference(eventTopic).child("rsvps");
            eventParticipantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot participantSnapshot : dataSnapshot.getChildren()) {
                            String userId = participantSnapshot.getKey();
                            retrieveUserFCMTokenAndSendNotification(userId);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Database", "Error retrieving event participants: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("Database", "Event topic is null");
        }
    }


    private void retrieveAndSendNotificationsForAllUsers() {
        DatabaseReference allUserTokensRef = FirebaseDatabase.getInstance().getReference("UserFCMTokens");
        allUserTokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userTokenSnapshot : dataSnapshot.getChildren()) {
                        String userFCMToken = userTokenSnapshot.getValue(String.class);
                        sendFCMNotification(userFCMToken, announcement.getTitle(), announcement.getDescription());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database", "Error retrieving all user tokens: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotification(String title, String details) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel_id",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default_channel_id")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(details)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    private void retrieveUserFCMTokenAndSendNotification(String userId) {
        DatabaseReference userFCMTokenRef = FirebaseDatabase.getInstance().getReference("UserFCMTokens").child(userId);
        userFCMTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userFCMToken = dataSnapshot.getValue(String.class);
                    sendFCMNotification(userFCMToken, announcement.getTitle(), announcement.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database", "Error retrieving user FCM token: " + databaseError.getMessage());
            }
        });
    }

    private void sendFCMNotification(String userFCMToken, String title, String details) {
        String serverKey = "AAAAhExjLj8:APA91bFuA8VZGbZ8OZlgKxu9DOGIdBTmJbU9L36sfyQmV0mDAv6apgh0O-tWbnsRCyFi_Xq6lPZYzP16JaL2-tFCcJsu2wJTt808m2GjCgvbvBCDLYsLGRmRDmWBKiyuvT2ZDQLhUk0n";

        JSONObject message = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("title", title);
            data.put("details", details);
            message.put("data", data);
            message.put("to", userFCMToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=" + serverKey);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(message.toString().getBytes("UTF-8"));
                outputStream.close();

                int responseCode = conn.getResponseCode();
                Log.d("FCM Response", "Response Code: " + responseCode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
