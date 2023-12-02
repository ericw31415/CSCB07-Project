package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.utoronto.cscb07project.R;

public class AddAnnouncmentFragment extends Fragment {

    private EditText titleEditText;
    private EditText descriptionEditText;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_announcment, container, false);

        // Initialize UI elements
        titleEditText = view.findViewById(R.id.AnnouncementTitleEditText);
        descriptionEditText = view.findViewById(R.id.AnnouncementDescriptionEditText);
        Button submitButton = view.findViewById(R.id.postAnnouncementButton);

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> postAnnouncement());

        return view;
    }

    private void postAnnouncement() {
        // Collect input data
        String title = titleEditText.getText().toString().trim();
        String details = descriptionEditText.getText().toString().trim();

        // Validate input
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        Date currentDate = new Date();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Submit the complaint to Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference announcementsRef = database.getReference("Announcements");
        String AnnouncementId = announcementsRef.push().getKey();
        if (AnnouncementId != null) {
            Announcement announcement = new Announcement(title, formattedDate, details, AnnouncementId);
            announcementsRef.child(AnnouncementId).setValue(announcement)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Announcement posted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Post failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link AnnouncmenetDetailFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class AnnouncmenetDetailFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public AnnouncmenetDetailFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnnouncmenetDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static AnnouncmenetDetailFragment newInstance(String param1, String param2) {
            AnnouncmenetDetailFragment fragment = new AnnouncmenetDetailFragment();
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
            return inflater.inflate(R.layout.fragment_announcmenet_detail, container, false);
        }
    }
}