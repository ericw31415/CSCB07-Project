package ca.utoronto.cscb07project.ui.login;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class LoginFragmentModel {
    private FirebaseAuth firebaseAuth;

    public LoginFragmentModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void tryLogin(String email, String password, LoginResponse response) {
        if (email.isEmpty() || password.isEmpty()) {
            response.inputError();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Test", "Success");
                        response.loginSuccess();
                    } else {
                        response.loginFailure(task.getException().getMessage());
                    }
                });
        }
    }
}
