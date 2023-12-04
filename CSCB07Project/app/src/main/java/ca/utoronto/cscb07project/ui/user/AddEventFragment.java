package ca.utoronto.cscb07project.ui.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.announcements.Announcement;
import ca.utoronto.cscb07project.events.Event;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
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
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String title;
    private String dateTime;
    private String location;
    private String description;
    private TextView eventTitle;
    private TextView eventDesc;
    private TextView eventLocation;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void showDateTimePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Handle selected date
            String selectedDate = formatDate(selection);
            showTimePicker(selectedDate);
        });

        datePicker.show(getChildFragmentManager(), "Date Picker");
    }

    private void showTimePicker(String selectedDate) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("Select Time")
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            String selectedTime = formatTime(timePicker.getHour(), timePicker.getMinute());
            String selectedDateTime = selectedDate + " " + selectedTime;
            this.dateTime = selectedDateTime;
            Log.d("Time", selectedDateTime);
        });

        timePicker.show(getChildFragmentManager(), "Time Picker");
    }

    private String formatDate(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(milliseconds));
    }

    private String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        Button btnDateTimePicker = view.findViewById(R.id.btnDatePicker);
        btnDateTimePicker.setOnClickListener(v -> showDateTimePicker());
        Button btnAddEvent = view.findViewById(R.id.addEventButton);
        this.eventTitle = view.findViewById(R.id.eventTitle);
        this.eventDesc = view.findViewById(R.id.eventDescription);
        this.eventLocation = view.findViewById(R.id.eventLocation);

        btnAddEvent.setOnClickListener(v->addEvent());
        return view;
    }

    private void addEvent() {
        if (eventTitle.getText().toString().isEmpty() ||
                eventDesc.getText().toString().isEmpty() ||
                eventLocation.getText().toString().isEmpty() ||
                dateTime == null) {
            Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
        } else {
            String title = this.eventTitle.getText().toString();
            String location = this.eventLocation.getText().toString();
            String description = this.eventDesc.getText().toString();
            Log.d("", dateTime);

            // Generate a unique ID for the event
            String eventId = Event.generateUniqueId();
            sendPushNotification(title, description);

            // Create the Event with the generated ID
            Event event = new Event(eventId, title, dateTime, location, description);

            // Set the event in the "Events" level with the same ID
            DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Events");
            eventsRef.child(eventId).setValue(event);

            String announcementTitle = "NEW EVENT: " + title;
            String announcmentDescrption = description;
            String announcementDate = dateTime;
            String announcementID = Announcement.generateUniqueID();

            Announcement announcement = new Announcement(announcementTitle, announcementDate, announcmentDescrption, announcementID, eventId);
            DatabaseReference announcementsRef = FirebaseDatabase.getInstance().getReference("Announcements");
            announcementsRef.child(announcementID).setValue(announcement);
            sendPushNotificationToAllUsers(announcementTitle, announcmentDescrption);
        }

    }

    private void sendPushNotificationToAllUsers(String title1, String details1) {
        // Fetch all user FCM tokens from the database
        DatabaseReference allUserTokensRef = FirebaseDatabase.getInstance().getReference("UserFCMTokens");
        allUserTokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userTokenSnapshot : dataSnapshot.getChildren()) {
                        String userFCMToken = userTokenSnapshot.child("token").getValue(String.class);
                        sendFCMNotification(userFCMToken, title1, details1);
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
        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel_id",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Construct the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default_channel_id")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title) // Use the announcement title as notification title
                .setContentText(details) // Use the announcement details as notification content
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Display the notification
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
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