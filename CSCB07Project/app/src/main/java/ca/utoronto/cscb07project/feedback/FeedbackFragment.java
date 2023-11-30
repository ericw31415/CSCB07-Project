package ca.utoronto.cscb07project.feedback;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentFeedbackBinding;
import ca.utoronto.cscb07project.ui.events.EventsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {

    private FragmentFeedbackBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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

    public DatabaseReference userRef;
    public FirebaseUser currUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);

        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get current user's ID
                currUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currUser.getUid();
                userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                //get user input: event, rating, feedback
                String event = binding.editTextText2.getText().toString();
                float rating = binding.ratingBar.getRating();
                String feedback = binding.editTextTextMultiLine.getText().toString();

                //feedback object
                Feedback userFeedback = new Feedback(userRef, event, rating, feedback);

                //reference Firebase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference feedbackRef = database.getReference("feedback");//change to refer to where we want feedback stored

                //push feedback to firebase
                feedbackRef.push().setValue(userFeedback);

                // Placeholder message
                Toast myToast = Toast.makeText(getActivity(), event + " (" + rating + "): " + feedback, Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        return binding.getRoot();
    }
}