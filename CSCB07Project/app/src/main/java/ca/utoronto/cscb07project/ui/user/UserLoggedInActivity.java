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

        // Step 1: Retrieve and Store FCM Token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String fcmToken = task.getResult();
                        saveUserFCMToken(currentUser.getUid(), currentUser.getEmail(), fcmToken);
                    } else {
                        Log.e("FCM", "Error getting FCM token");
                    }

                });

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

                        // Subscribe the user to the "announcements" topic
                        subscribeToAnnouncementsTopic(currentUser.getUid());
                    }
                } else {
                    Log.d("User Data", "DataSnapshot does not exist");
                }
            }

            private void subscribeToAnnouncementsTopic(String userId) {
                String announcementsTopic = "Announcements";
                subscribeToTopic(userId, announcementsTopic);
            }

            private void subscribeToTopic(String userId, String topic) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Database Error: " + error.getMessage());
            }
        });


    }

    private void saveUserFCMToken(String userId, String userEmail, String fcmToken) {
        DatabaseReference userFCMTokenRef = FirebaseDatabase.getInstance().getReference("UserFCMTokens").child(userId);

        // Assuming you want to store the email along with the FCM token
        userFCMTokenRef.child("email").setValue(userEmail);

        // Storing the FCM token
        userFCMTokenRef.child("token").setValue(fcmToken);
    }


    // Other methods...

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

    // Other methods...
}
