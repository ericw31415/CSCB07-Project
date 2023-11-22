package ca.utoronto.cscb07project.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.loginsignout.LoggedInFragment;

public class UserActivity extends AppCompatActivity {

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
        loadFragment(new LoggedInFragment());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("fName").getValue(String.class);
                    String lastName = dataSnapshot.child("lName").getValue(String.class);
                    Boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);

                    if(isAdmin){
                        loadFragment(new AdminHome());
                    }else{
                        loadFragment(new UserHome());
                    }

                    ((TextView)findViewById(R.id.greeting)).setText("Hello " + firstName + " " + lastName + "!");
                    Log.d("IsAdmin", isAdmin.toString());
                    Log.d("User Data", "First Name: " + firstName + ", Last Name: " + lastName);
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
}