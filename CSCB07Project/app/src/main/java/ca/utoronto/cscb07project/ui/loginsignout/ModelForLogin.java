package ca.utoronto.cscb07project.ui.loginsignout;

public interface ModelForLogin {
    void login(String email, String password, loginResponse response);
    interface loginResponse {
        void loginSuccess();

        void loginFailure(String error);
    }

}
