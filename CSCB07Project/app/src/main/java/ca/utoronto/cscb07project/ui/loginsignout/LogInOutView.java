package ca.utoronto.cscb07project.ui.loginsignout;

public interface LogInOutView {
    void successfulLogin();
    void unsuccessfulLogin();
    void loggedOut();
    void invalidInput();
}
