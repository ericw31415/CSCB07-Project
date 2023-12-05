package ca.utoronto.cscb07project.ui.user;

import android.content.Intent;
import android.os.Bundle;
import ca.utoronto.cscb07project.events.AttendeesListActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.utoronto.cscb07project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserDataViewModel userDataViewModel;

    private TextView fnameTextView;

    public AdminHome() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHome.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHome newInstance(String param1, String param2) {
        AdminHome fragment = new AdminHome();
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
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        fnameTextView = view.findViewById(R.id.userfirstname);

        userDataViewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        userDataViewModel.getFirstName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String firstName) {
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

        userDataViewModel.getUserEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String email) {
                Log.d("Test", email);
            }
        });

        userDataViewModel.getIsAdmin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdmin) {
                Log.d("Test", isAdmin.toString());
            }
        });


        Button viewAttendeesButton = view.findViewById(R.id.viewAttendeesButton);
        viewAttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AttendeesListActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}