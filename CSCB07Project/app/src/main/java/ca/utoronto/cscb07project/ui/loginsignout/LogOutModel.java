package ca.utoronto.cscb07project.ui.loginsignout;

import com.google.firebase.auth.FirebaseAuth;

public class LogOutModel implements ModelForLogout {

    private FirebaseAuth firebaseAuth;

    public LogOutModel () {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void logOut(logOutResponse response) {
        firebaseAuth.signOut();
        response.successLogout();
    }
}
