package ca.utoronto.cscb07project.ui.complaints;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.R;

public class ComplaintFragment extends Fragment {

    private EditText titleEditText;
    private EditText detailsEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);

        // Initialize UI elements
        titleEditText = view.findViewById(R.id.complaintTitleEditText);
        detailsEditText = view.findViewById(R.id.complaintDetailsEditText);
        Button submitButton = view.findViewById(R.id.submitComplaintButton);

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> submitComplaint());

        return view;
    }

    private void submitComplaint() {
        // Collect input data
        String title = titleEditText.getText().toString().trim();
        String details = detailsEditText.getText().toString().trim();

        // Validate input
        if (title.isEmpty() || details.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Submit the complaint to Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference complaintsRef = database.getReference("Complaints");
        String complaintId = complaintsRef.push().getKey();
        if (complaintId != null) {
            complaints complaint = new complaints(complaintId, title, details);
            complaintsRef.child(complaintId).setValue(complaint)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Complaint submitted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}