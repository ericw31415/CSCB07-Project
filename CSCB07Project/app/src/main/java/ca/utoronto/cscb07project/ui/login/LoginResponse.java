package ca.utoronto.cscb07project.ui.login;

public interface LoginResponse {
    public void loginSuccess();

    public void loginFailure(String error);

    public void inputError();
}
