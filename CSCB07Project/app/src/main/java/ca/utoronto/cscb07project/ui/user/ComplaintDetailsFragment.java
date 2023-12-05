package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComplaintDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComplaintDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView complaintID;
    private TextView complaintTitle;
    private TextView complaintText;
    private TextView studentName;
    private TextView studentEmail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComplaintDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComplaintDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComplaintDetailsFragment newInstance(String param1, String param2) {
        ComplaintDetailsFragment fragment = new ComplaintDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_complaint_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            String studentEmail = args.getString("studentEmail");
            String title = args.getString("title");
            String details = args.getString("details");
            String date = args.getString("date");

            ((TextView) view.findViewById(R.id.studentEmail)).setText("Student Email: "+ studentEmail);
            ((TextView) view.findViewById(R.id.complaintTitle)).setText(title);
            ((TextView) view.findViewById(R.id.complaintText)).setText(details);
            ((TextView) view.findViewById(R.id.complaintDate)).setText("Sent On: " + date);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("users");
            Query query = usersRef.orderByChild("email").equalTo(studentEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String firstName = userSnapshot.child("fName").getValue(String.class);
                            String lastName = userSnapshot.child("lName").getValue(String.class);

                            ((TextView) view.findViewById(R.id.studentName)).setText("Student Name: "+ firstName + " " + lastName);
                        }
                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        return view;
    }
}