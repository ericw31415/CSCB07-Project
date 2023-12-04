package ca.utoronto.cscb07project.ui.login;

public interface LoginModel {
    void tryLogin(String email, String password, LoginResponse response);
}
