package ca.utoronto.cscb07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.databinding.ActivityMainBinding;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckActivity;
import ca.utoronto.cscb07project.ui.complaints.ComplaintActivity;
import ca.utoronto.cscb07project.ui.loginsignout.LoginSignoutActivity;
import ca.utoronto.cscb07project.ui.signup.SignupActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Button POStCheck;

    private Button complaintButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_events, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



    }

    public void goToLoginSignOut(View view) {
        Intent intent = new Intent(this, LoginSignoutActivity.class);
        startActivity(intent);
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    public void toComplaints(View view) {
        Intent intent = new Intent(this, ComplaintActivity.class);
        startActivity(intent);
    }

    public void goToPostCheck(View view) {
        startActivity(new Intent(this, POStCheckActivity.class));
    }
}