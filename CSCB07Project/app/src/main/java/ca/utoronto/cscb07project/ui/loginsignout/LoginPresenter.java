package ca.utoronto.cscb07project.ui.loginsignout;

public class LoginPresenter implements PresenterForLogin{
    private LogInOutView view;
    private LoginModel loginModel;
    private LogOutModel logOutModel;


    LoginPresenter(LogInOutView view){
        this.view = view;
        this.loginModel= new LoginModel();
        this.logOutModel = new LogOutModel();
    }

    @Override
    public void tryLogin(String email, String password) {
        loginModel.login(email, password, new ModelForLogin.loginResponse() {
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

    @Override
    public void tryLogOut() {
        logOutModel.logOut(() -> view.loggedOut());
    }

}
