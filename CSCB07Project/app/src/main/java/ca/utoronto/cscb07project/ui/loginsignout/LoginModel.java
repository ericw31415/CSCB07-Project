package ca.utoronto.cscb07project.ui.loginsignout;


import com.google.firebase.auth.FirebaseAuth;

public class LoginModel implements ModelForLogin{

    private FirebaseAuth firebaseAuth;

    public LoginModel(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, loginResponse response) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        response.loginSuccess();
                    } else {
                        response.loginFailure(task.getException().getMessage());
                    }
                });
    }
}
