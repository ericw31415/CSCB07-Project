package ca.utoronto.cscb07project.ui.login;

public interface LoginResponse {
    void loginSuccess();

    void loginFailure();

    void inputError();
}
