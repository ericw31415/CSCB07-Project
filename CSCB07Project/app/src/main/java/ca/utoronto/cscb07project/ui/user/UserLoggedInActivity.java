package ca.utoronto.cscb07project.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Set;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.announcements.All_Announcement_Fragment;
import ca.utoronto.cscb07project.events.EventListFragment;
import ca.utoronto.cscb07project.ui.POStCheck.MajOrMin;
import ca.utoronto.cscb07project.ui.complaints.ComplaintActivity;

public class UserLoggedInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    // Keep track of subscribed topics to simulate unsubscription
    private Set<String> subscribedTopics = new HashSet<>();

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.userFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        loadFragment(new LoadingFragment());

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        this.mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(currentUser.getUid());
        Log.d("ID", currentUser.getUid().toString());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("fName").getValue(String.class);
                    String lastName = dataSnapshot.child("lName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    Boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);

                    userDataViewModel.setUserInfo(firstName, lastName, email, isAdmin);

                    if (isAdmin) {
                        loadFragment(new AdminHome());
                    } else {
                        loadFragment(new UserHome());

                        // Unsubscribe from all topics before subscribing again
                        unsubscribeFromAllTopics();

                        // Subscribe the user to the "announcements" topic
                        subscribeToAnnouncementsTopic();

                        // Subscribe the user to event topics based on RSVP
                        subscribeToEventTopics(currentUser.getUid());
                    }
                } else {
                    Log.d("User Data", "DataSnapshot does not exist");
                }
            }

            private void subscribeToAnnouncementsTopic() {
                String announcementsTopic = "Announcements";
                FirebaseMessaging.getInstance().subscribeToTopic(announcementsTopic)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                subscribedTopics.add(announcementsTopic);
                                Log.d("FCM", "Subscribed to announcements topic");
                            } else {
                                Log.e("FCM", "Subscription to announcements topic failed");
                            }
                        });
            }

            private void subscribeToEventTopics(String userId) {
                DatabaseReference userEventsRef = database.getReference("UserEvents").child(userId);
                userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            String eventId = eventSnapshot.getKey();
                            if (eventId != null) {
                                checkRSVPAndSubscribeToTopic(eventId, userId);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Error", "Database Error: " + error.getMessage());
                    }
                });
            }

            private void checkRSVPAndSubscribeToTopic(String eventId, String userId) {
                DatabaseReference rsvpsRef = database.getReference("Events").child(eventId).child("rsvps");
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String currentUserEmail = currentUser.getEmail();

                    if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
                        rsvpsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String email = childSnapshot.getValue(String.class);

                                    // Check if the retrieved email matches the current user's email
                                    if (email != null && email.equals(currentUserEmail)) {
                                        subscribeToTopic(eventId);
                                        Log.d("RSVP", "User has RSVP'd to event: " + eventId);
                                        return;  // Exit the loop early since you found a match
                                    }
                                }

                                // If the loop completes without finding a match, the user hasn't RSVP'd
                                Log.d("RSVP", "User has not RSVP'd to event: " + eventId);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("Error", "Database Error: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            private void subscribeToTopic(String topic) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                subscribedTopics.add(topic);
                                Log.d("FCM", "Subscribed to topic: " + topic);
                            } else {
                                Log.e("FCM", "Subscription to topic " + topic + " failed");
                            }
                        });
            }

            private void unsubscribeFromAllTopics() {
                for (String topic : subscribedTopics) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    Log.d("FCM", "Unsubscribed from topic: " + topic);
                }
                subscribedTopics.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Database Error: " + error.getMessage());
            }
        });
    }

    /**
     * The rest of your UserLoggedInActivity methods...
     */

    public void goToMajOrMin(View view) {
        startActivity(new Intent(this, MajOrMin.class));
    }

    public void toComplaints(View view) {
        Intent intent = new Intent(this, ComplaintActivity.class);
        startActivity(intent);
    }

    // Other methods...

    public void goToAddEvents(View view) {
        loadFragment(new AddEventFragment());
    }

    public void toEvents(View view) {
        loadFragment(new EventListFragment());
    }

    public void toAttendingEvents(View view) {
        loadFragment(new UserAttendingEvents());
    }

    public void goAddAnnouncements(View view) {
        loadFragment(new AdminAnnouncements());
    }

    public void toUserAnnouncements(View view) {
        loadFragment(new All_Announcement_Fragment());
    }

}
