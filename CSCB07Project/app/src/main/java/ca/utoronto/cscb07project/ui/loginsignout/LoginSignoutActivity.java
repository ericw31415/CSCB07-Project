package ca.utoronto.cscb07project.ui.loginsignout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.signup.UserModel;

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
        transaction.replace(R.id.loginFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void tryLogin(View view) {
        String email = ((TextView)findViewById(R.id.emailText)).getText().toString();
        String password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
        presenter.tryLogin(email, password);
    }

    @Override
    public void successfulLogin() {
        Log.d("success", "logged in good");
    }

    @Override
    public void unsuccessfulLogin() {
        Log.d("failure", "logged in not good");
    }
}