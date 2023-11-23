package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import ca.utoronto.cscb07project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private UserDataViewModel userDataViewModel;

    private TextView fnameTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHome.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHome newInstance(String param1, String param2) {
        UserHome fragment = new UserHome();
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
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        fnameTextView = view.findViewById(R.id.userfirstname);

        // Inflate the layout for this fragment
        userDataViewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        // Observe changes to first name
        userDataViewModel.getFirstName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String firstName) {
                // Update UI with the first name d
                userDataViewModel.getLastName().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String lastName) {
                        Log.d("Test", lastName);
                        fnameTextView.setText(firstName +" " + lastName);
                    }
                });
                Log.d("Test", firstName);
            }
        });

        // Observe changes to email
        userDataViewModel.getUserEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String email) {
                // Update UI with the email
                Log.d("Test", email);
            }
        });

        // Observe changes to isAdmin
        userDataViewModel.getIsAdmin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdmin) {
                // Update UI with the isAdmin status
                Log.d("Test", isAdmin.toString());
            }
        });

        return view;
    }
}