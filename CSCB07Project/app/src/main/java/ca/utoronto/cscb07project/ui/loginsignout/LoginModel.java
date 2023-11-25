package ca.utoronto.cscb07project.ui.loginsignout;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class LoginModel implements ModelForLogin{

    private FirebaseAuth firebaseAuth;

    public LoginModel(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, loginResponse response) {
        if(email.isEmpty() || password.isEmpty()){
            response.inputError();
        }
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
