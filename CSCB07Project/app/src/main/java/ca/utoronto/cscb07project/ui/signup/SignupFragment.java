package ca.utoronto.cscb07project.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentSignupBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    private FragmentSignupBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false);

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        return binding.getRoot();
    }

    public void signup() {
        String email = binding.emailAddress.getText().toString().trim();
        String firstName = binding.firstName.getText().toString().trim();
        String lastName = binding.lastName.getText().toString().trim();
        String password = binding.password.getText().toString();
        boolean isAdmin = binding.adminSwitch.isChecked();

        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty())
            Toast.makeText(getActivity(), "Make sure all fields are filled!", Toast.LENGTH_SHORT).show();
        else
            createUser(new UserModel(email, firstName, lastName, password, isAdmin));
    }

    public void createUser(UserModel user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fUser = auth.getCurrentUser();
                            usersRef.child(fUser.getUid()).setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Firebase", "Account creation success");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Firebase", "Account creation failure");
                                        }
                                    });
                            Toast.makeText(getActivity(), "Account created", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(SignupFragment.this)
                                    .popBackStack(R.id.navigation_login, false);
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthWeakPasswordException)
                                Toast.makeText(getActivity(), "Please use a stronger password.", Toast.LENGTH_SHORT).show();
                            else if (exception instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(getActivity(), "There is already an account associated with this user.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "An account could not be created.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}