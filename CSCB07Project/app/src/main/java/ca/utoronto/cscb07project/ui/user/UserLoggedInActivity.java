package ca.utoronto.cscb07project.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.events.*;
import ca.utoronto.cscb07project.ui.POStCheck.MajOrMin;
import ca.utoronto.cscb07project.ui.complaints.ComplaintActivity;

public class UserLoggedInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

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
                    }
                } else {
                    Log.d("User Data", "DataSnapshot does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Database Error: " + error.getMessage());
            }
        });
    }

    /**
    public void goToPostCheck(View view) {
        startActivity(new Intent(this, POStCheckActivity.class));
    }
     */
    public void goToMajOrMin(View view) {
        startActivity(new Intent(this, MajOrMin.class));
    }

    public void toComplaints(View view) {
        Intent intent = new Intent(this, ComplaintActivity.class);
        startActivity(intent);
    }

    public void toAdminComplaints(View view){
        loadFragment(new AdminComplaintsFragment());
    }

    public void toAdminEventFeedback(View view){
        loadFragment(new AdminEventsFeedback());
    }

    public void logOut(View view) {
        /*
        mAuth.signOut();
        Toast.makeText(this,"You are now logged out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
         */
    }

    public void goToAddEvents(View view) {
        loadFragment(new AddEventFragment());
    }

    public void toEvents(View view) {
        loadFragment(new EventListFragment());
    }
}