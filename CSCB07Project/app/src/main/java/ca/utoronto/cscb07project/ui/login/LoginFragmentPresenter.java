package ca.utoronto.cscb07project.ui.login;

public class LoginFragmentPresenter {
    private LoginFragmentView view;
    private LoginFragmentModel loginModel;

    public LoginFragmentPresenter(LoginFragmentView view) {
        this.view = view;
        this.loginModel = new LoginFragmentModel();
    }

    public void tryLogin(String email, String password) {
        loginModel.tryLogin(email, password, new LoginResponse() {
            @Override
            public void loginSuccess() {
                view.successfulLogin();
            }

            @Override
            public void loginFailure(String error) {
                view.unsuccessfulLogin();
            }

            @Override
            public void inputError() {
                view.invalidInput();
            }
        });
    }
}
