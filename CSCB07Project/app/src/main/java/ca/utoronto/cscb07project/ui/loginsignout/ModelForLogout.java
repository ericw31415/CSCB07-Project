package ca.utoronto.cscb07project.ui.loginsignout;

public interface ModelForLogout {
    void logOut(logOutResponse response);
    interface logOutResponse{
        void successLogout();
    }
}
