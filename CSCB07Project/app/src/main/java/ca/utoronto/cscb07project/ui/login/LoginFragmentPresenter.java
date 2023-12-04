package ca.utoronto.cscb07project.ui.login;

public class LoginFragmentPresenter {
    private final LoginView view;
    private final LoginModel model;

    public LoginFragmentPresenter(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    public void tryLogin(String email, String password) {
        LoginResponse response = view.getLoginResponse();
        if (email.isEmpty() || password.isEmpty()) {
            response.inputError();
            return;
        }
        model.tryLogin(email, password, response);
    }
}
