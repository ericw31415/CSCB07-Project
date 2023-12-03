package ca.utoronto.cscb07project.ui.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.utoronto.cscb07project.R;

public class ComplaintFragment extends Fragment {

    private EditText titleEditText;
    private EditText complaintDetailsEditText;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);

        // Initialize UI elements
        titleEditText = view.findViewById(R.id.complaintTitleEditText);
        complaintDetailsEditText = view.findViewById(R.id.complaintDetailsEditText);
        Button submitButton = view.findViewById(R.id.submitComplaintButton);

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> submitComplaint());

        return view;
    }

    private void submitComplaint() {
        // Collect input data
        String title = titleEditText.getText().toString().trim();
        String details = complaintDetailsEditText.getText().toString().trim();

        // Validate input
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        Date currentDate = new Date();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Submit the complaint to Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference complaintsRef = database.getReference("complaints");
        String complaintId = complaintsRef.push().getKey();
        if (complaintId != null) {
            complaints complaint = new complaints(complaintId, title, email, formattedDate, details);
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