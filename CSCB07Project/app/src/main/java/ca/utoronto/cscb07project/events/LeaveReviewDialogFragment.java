package ca.utoronto.cscb07project.events;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.R;

public class LeaveReviewDialogFragment extends DialogFragment {

    private static final String ARG_EVENT_ID = "event_id";

    private String eventId;
    private EditText reviewEditText;
    private NumberPicker ratingNumberPicker;

    public LeaveReviewDialogFragment() {
        // Required empty public constructor
    }

    public static LeaveReviewDialogFragment newInstance(String eventId) {
        LeaveReviewDialogFragment fragment = new LeaveReviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_leave_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
        }

        reviewEditText = view.findViewById(R.id.reviewEditText);
        ratingNumberPicker = view.findViewById(R.id.ratingNumberPicker);

        ratingNumberPicker.setMinValue(1);
        ratingNumberPicker.setMaxValue(5);

        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        String reviewText = reviewEditText.getText().toString().trim();
        int rating = ratingNumberPicker.getValue();

        // Get current user's email
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Initialize the DatabaseReference
            DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("Reviews");

            // Create a unique key for the review
            String reviewId = reviewsRef.push().getKey();

            // Create a Review object with the data
            Review review = new Review(userEmail, eventId, reviewText, rating);

            // Save the review data to the database
            if (reviewId != null) {
                reviewsRef.child(reviewId).setValue(review);
            }

            // Dismiss the dialog
            dismiss();
        }
    }
}
