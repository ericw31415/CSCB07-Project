package ca.utoronto.cscb07project.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.loginsignout.LoginUserFragment;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loadFragment(new SignUpFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.userFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void signUp(View view) {
        EditText email = findViewById(R.id.emailAddress);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText password = findViewById(R.id.password);
        Switch adminSwitch = findViewById(R.id.adminSwitch);

        String userEmail = email.getText().toString().trim();
        String userFirstName = firstName.getText().toString().trim();
        String userLastName = lastName.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        boolean isAdmin = adminSwitch.isChecked();

        if(userEmail.isEmpty() || userFirstName.isEmpty() || userLastName.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(this,"Make sure all fields are filled!", Toast.LENGTH_SHORT).show();
        }else{
            createUser(new UserModel(userEmail, userPassword, userFirstName, userLastName, isAdmin));
        }
    }

    public void createUser(UserModel user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser fUser = auth.getCurrentUser();
                        usersRef.child(fUser.getUid()).setValue(user)
                                .addOnSuccessListener(this, newTask ->{
                                    Log.d("Success", "Success");
                                })
                                .addOnFailureListener(userCreateError -> {
                                    Log.d("error", "Error adding node");
                                });
                    }
                    else{
                        Log.d("error", "error");
                    }
                });
        loadFragment(new LoginUserFragment());
        Toast.makeText(this, "Account Created! Login!", Toast.LENGTH_SHORT).show();
    }








}