package ca.utoronto.cscb07project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.util.List;

import ca.utoronto.cscb07project.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp secondaryApp = null;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(this);
        for(FirebaseApp app : firebaseApps){
            if(app.getName().equals("secondary")){
                secondaryApp = app;
                break;
            }
        }

        if (secondaryApp == null) {
            FirebaseOptions secondaryOptions = new FirebaseOptions.Builder()
                    .setApplicationId("1:568217251391:android:25b9a28033be47e2e8528a")
                    .setApiKey("AIzaSyCp9Pgw40WZbdaBZSVvyLnWhjrt-_ImNHE")
                    .setDatabaseUrl("https://cscb07-group-default-rtdb.firebaseio.com/")
                    .setProjectId("cscb07-group")
                    .build();

            secondaryApp = FirebaseApp.initializeApp(this, secondaryOptions, "secondary");
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        final NavController navController = navHostFragment.getNavController(); // Make navController final
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

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_events, R.id.navigation_notifications)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
