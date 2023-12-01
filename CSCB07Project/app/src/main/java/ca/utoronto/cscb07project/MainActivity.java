package ca.utoronto.cscb07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.utoronto.cscb07project.databinding.ActivityMainBinding;
import ca.utoronto.cscb07project.ui.events.EventsFragment;
import ca.utoronto.cscb07project.ui.home.HomeFragment;
import ca.utoronto.cscb07project.ui.loginsignout.LoginActivity;
import ca.utoronto.cscb07project.ui.notifications.NotificationsFragment;
import ca.utoronto.cscb07project.ui.signup.SignupActivity;
import ca.utoronto.cscb07project.ui.user.All_Announcement_Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_notifications) {
                loadFragment(new All_Announcement_Fragment());
            } else if (itemId == R.id.navigation_events) {
                loadFragment(new EventsFragment());
            } else if (itemId == R.id.navigation_home) {
                loadFragment(new HomeFragment());
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

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFrame, fragment);
        transaction.commit();
    }
}