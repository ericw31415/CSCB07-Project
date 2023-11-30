package ca.utoronto.cscb07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import ca.utoronto.cscb07project.databinding.ActivityMainBinding;
import ca.utoronto.cscb07project.ui.events.EventsFragment;
import ca.utoronto.cscb07project.ui.home.HomeFragment;
import ca.utoronto.cscb07project.ui.loginsignout.LoginActivity;
import ca.utoronto.cscb07project.ui.notifications.NotificationsFragment;
import ca.utoronto.cscb07project.ui.signup.SignupActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseOptions secondaryOptions = new FirebaseOptions.Builder()
                .setApplicationId("1:568217251391:android:25b9a28033be47e2e8528a")
                .setApiKey("AIzaSyCp9Pgw40WZbdaBZSVvyLnWhjrt-_ImNHE")
                .setDatabaseUrl("https://cscb07-group-default-rtdb.firebaseio.com/")
                .setProjectId("cscb07-group")
                .build();

        FirebaseApp.initializeApp(this, secondaryOptions, "secondary");
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_notifications) {
                navController.navigate(R.id.navigation_notifications);
            } else if (itemId == R.id.navigation_events) {
                navController.navigate(R.id.navigation_events);
            } else if (itemId == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
            }
            return true;
        });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }


}