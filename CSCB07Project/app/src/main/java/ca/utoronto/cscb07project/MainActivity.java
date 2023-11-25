package ca.utoronto.cscb07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.utoronto.cscb07project.databinding.ActivityMainBinding;
import ca.utoronto.cscb07project.ui.POStCheck.MajOrMin;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckActivity;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckCalc;
import ca.utoronto.cscb07project.ui.events.EventsFragment;
import ca.utoronto.cscb07project.ui.home.HomeFragment;
import ca.utoronto.cscb07project.ui.loginsignout.LoginSignoutActivity;
import ca.utoronto.cscb07project.ui.notifications.NotificationsFragment;
import ca.utoronto.cscb07project.ui.signup.SignupActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
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

    /**
    public void goToPostCheck(View view) {
        startActivity(new Intent(this, POStCheckActivity.class));
    }
     */

    public void goToMajOrMin(View view) {
        startActivity(new Intent(this, MajOrMin.class));
    }
}