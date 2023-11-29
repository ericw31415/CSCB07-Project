package ca.utoronto.cscb07project.ui.login;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentFeedbackBinding;
import ca.utoronto.cscb07project.databinding.FragmentLoginBinding;
import ca.utoronto.cscb07project.ui.home.HomeFragment;
import ca.utoronto.cscb07project.ui.signup.SignupActivity;
import ca.utoronto.cscb07project.ui.user.UserLoggedInActivity;

public class LoginFragmentView extends Fragment {
    private FragmentLoginBinding binding;
    private LoginFragmentPresenter presenter;

    public LoginFragmentView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailText.getText().toString();
                String password = binding.passwordText.getText().toString();
                presenter.tryLogin(email, password);
            }
        });

        return binding.getRoot();
    }

    public void successfulLogin() {
        Toast.makeText(getActivity(), "Good login", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, UserLoggedInActivity.class);
        // startActivity(intent);
    }

    public void unsuccessfulLogin() {
        Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
        Log.d("failure", "logged in not good");
    }

    public void invalidInput() {
        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
    }
}