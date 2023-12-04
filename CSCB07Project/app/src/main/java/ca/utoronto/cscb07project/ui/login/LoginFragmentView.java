package ca.utoronto.cscb07project.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentLoginBinding;
import ca.utoronto.cscb07project.ui.user.UserLoggedInActivity;

public class LoginFragmentView extends Fragment implements LoginView {
    private LoginFragmentPresenter presenter;

    private FragmentLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginFragmentPresenter(this, new LoginFragmentModel());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailText.getText().toString().trim();
                String password = binding.passwordText.getText().toString();
                presenter.tryLogin(email, password);
            }
        });

        binding.tologin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragmentView.this)
                    .navigate(R.id.action_navigation_login_to_navigation_signup);
            }
        });

        return binding.getRoot();
    }

    @Override
    public LoginResponse getLoginResponse() {
        return new LoginResponse() {
            @Override
            public void loginSuccess() {
                // TODO: Use a fragment instead
                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserLoggedInActivity.class);
                startActivity(intent);
            }

            @Override
            public void loginFailure() {
                Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void inputError() {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
