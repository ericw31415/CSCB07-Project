package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentFeedbackBinding;
import ca.utoronto.cscb07project.feedback.Feedback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminEventsFeedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminEventsFeedback extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminEventsFeedback() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminEventsFeedback.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminEventsFeedback newInstance(String param1, String param2) {
        AdminEventsFeedback fragment = new AdminEventsFeedback();
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

    FragmentFeedbackBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        //inflater.inflate(R.layout.fragment_admin_events_feedback, container, false);

        //reference firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference feedbackRef = database.getReference("feedback");

        //listener to pull data
        feedbackRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    Feedback feedback = snap.getValue(Feedback.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}