package ca.utoronto.cscb07project.ui.user;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.announcements.Announcement;

public class AddAnnouncmentFragment extends Fragment {

    private EditText titleEditText;
    private EditText descriptionEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default_channel_id", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcment, container, false);

        titleEditText = view.findViewById(R.id.AnnouncementTitleEditText);
        descriptionEditText = view.findViewById(R.id.AnnouncementDescriptionEditText);
        Button submitButton = view.findViewById(R.id.postAnnouncementButton);

        submitButton.setOnClickListener(v -> postAnnouncement());

        return view;
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
        String announcementId = announcementsRef.push().getKey();

        if (announcementId != null) {
            Announcement announcement = new Announcement(title, formattedDate, details, announcementId);

            announcementsRef.child(announcementId).setValue(announcement)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Announcement posted", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();

                            // Trigger a notification when the announcement is successfully posted
                            sendNotification("New Announcement", "Check out the latest announcement!");
                        } else {
                            Toast.makeText(getContext(), "Post failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void sendNotification(String title, String message) {
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default_channel_id")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
