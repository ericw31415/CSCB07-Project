package ca.utoronto.cscb07project.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import ca.utoronto.cscb07project.MainActivity;
import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentLoginBinding;
import ca.utoronto.cscb07project.ui.home.HomeFragment;
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

    public void successfulLogin() {
        // TODO: Use a fragment instead
        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), UserLoggedInActivity.class);
        startActivity(intent);
    }

    public void unsuccessfulLogin() {
        Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
        Log.d("failure", "logged in not good");
    }

    public void invalidInput() {
        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
    }
}