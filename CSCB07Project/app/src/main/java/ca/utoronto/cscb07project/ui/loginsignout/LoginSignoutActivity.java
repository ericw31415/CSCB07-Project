package ca.utoronto.cscb07project.ui.loginsignout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.signup.UserModel;

public class LoginSignoutActivity extends AppCompatActivity implements LoginSignoutView{

    UserModel user;
    LoginSignoutView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signout);
        loadFragment(new LoginUserFragment());

    }

    public void tryLogin(View view) {
        Toast.makeText(this, "Login Attempt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successfulSignIn() {
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedSignIn() {

    }

    @Override
    public void SignOut() {

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.loginFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}