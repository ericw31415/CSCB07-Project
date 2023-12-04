package ca.utoronto.cscb07project.ui.login;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class LoginFragmentModel implements LoginModel {
    private static final String TAG = "LoginFragmentModel";
    private final FirebaseAuth auth;

    public LoginFragmentModel() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void tryLogin(String email, String password, LoginResponse response) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Successful login");
                    response.loginSuccess();
                } else {
                    Log.w(TAG, "Login failure", task.getException());
                    response.loginFailure();
                }
            });
    }
}
