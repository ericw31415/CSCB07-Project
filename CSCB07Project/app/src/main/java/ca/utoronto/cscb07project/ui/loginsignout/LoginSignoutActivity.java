package ca.utoronto.cscb07project.ui.loginsignout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.user.UserLoggedInActivity;

public class LoginSignoutActivity extends AppCompatActivity implements LogInOutView{

    LoginPresenter presenter;

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
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void tryLogin(View view) {
        String email = ((TextView)findViewById(R.id.emailText)).getText().toString();
        String password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
        presenter.tryLogin(email, password);
    }

    public void toSignOut(View view) {
        presenter.tryLogOut();
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
    public void loggedOut() {
        loadFragment(new LoginUserFragment());
        Toast.makeText(this, "You are logged out!", Toast.LENGTH_SHORT).show();
        Log.d("Log", "Logging Out");
    }

    @Override
    public void invalidInput() {

    }
}