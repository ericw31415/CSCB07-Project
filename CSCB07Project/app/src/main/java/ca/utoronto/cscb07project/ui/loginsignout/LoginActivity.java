package ca.utoronto.cscb07project.ui.loginsignout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.ActivityMainBinding;
import ca.utoronto.cscb07project.ui.events.EventsFragment;
import ca.utoronto.cscb07project.ui.home.HomeFragment;
import ca.utoronto.cscb07project.ui.notifications.NotificationsFragment;
import ca.utoronto.cscb07project.ui.signup.SignUpFragment;
import ca.utoronto.cscb07project.ui.signup.SignupActivity;
import ca.utoronto.cscb07project.ui.user.UserLoggedInActivity;

public class LoginActivity extends AppCompatActivity implements LogInOutView{

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signout);
        loadFragment(new LoginUserFragment());
        presenter = new LoginPresenter(this);
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.userFrame, fragment);
        transaction.commit();
    }

    public void tryLogin(View view) {
        String email = ((TextView)findViewById(R.id.emailText)).getText().toString();
        String password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
        presenter.tryLogin(email, password);
    }

    @Override
    public void successfulLogin() {
        Intent intent = new Intent(this, UserLoggedInActivity.class);
        startActivity(intent);
    }
    @Override
    public void unsuccessfulLogin() {
        Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
        Log.d("failure", "logged in not good");
    }

    @Override
    public void invalidInput() {
        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}