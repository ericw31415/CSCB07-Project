package ca.utoronto.cscb07project;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ca.utoronto.cscb07project.ui.login.LoginFragmentPresenter;
import ca.utoronto.cscb07project.ui.login.LoginModel;
import ca.utoronto.cscb07project.ui.login.LoginResponse;
import ca.utoronto.cscb07project.ui.login.LoginView;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
    final String EMAIL = "test@gmail.com";
    final String PASSWORD = "test123";

    LoginFragmentPresenter presenter;

    @Mock
    LoginView view;

    @Mock
    LoginModel model;

    @Mock
    LoginResponse response;

    @Before
    public void setup() {
        presenter = new LoginFragmentPresenter(view, model);
        when(view.getLoginResponse()).thenReturn(response);
    }

    @Test
    public void testEmptyEmail() {
        presenter.tryLogin("", PASSWORD);
        verify(response).inputError();
    }

    @Test
    public void testEmptyPassword() {
        presenter.tryLogin(EMAIL, "");
        verify(response).inputError();
    }

    @Test
    public void testGoodLogin() {
        presenter.tryLogin(EMAIL, PASSWORD);
        verify(response, times(0)).inputError();
    }
}
