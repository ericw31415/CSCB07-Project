package ca.utoronto.cscb07project.ui.loginsignout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public void toSignOut(View view) {
        presenter.tryLogOut();
    }

    @Override
    public void successfulLogin() {
        Log.d("success", "logged in good");
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
                    ((TextView)findViewById(R.id.greeting)).setText("Hello " + firstName + " " + lastName + "!");
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
    @Override
    public void unsuccessfulLogin() {
        Log.d("failure", "logged in not good");
    }

    @Override
    public void loggedOut() {
        loadFragment(new LoginUserFragment());
        Toast.makeText(this, "You are logged out!", Toast.LENGTH_SHORT).show();
        Log.d("Log", "Logging Out");
    }
}