package ca.utoronto.cscb07project.announcements;

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
import com.google.firebase.database.ValueEventListener;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.user.ComplaintDetailsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComplaintDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Announcement_DetailFragment extends Fragment {

    private static final String ARG_ANNOUNCEMENT_ID = "announcement_id";

    private TextView AnnouncementID;
    private TextView AnnouncementTitle;
    private TextView AnnouncementText;
    private TextView AnnouncementName;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Announcement_DetailFragment() {
        // Required empty public constructor
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
    public static Announcement_DetailFragment newInstance(String param1, String param2) {
        Announcement_DetailFragment fragment = new Announcement_DetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_announcement__detail, container, false);
        Bundle args = getArguments();

        if (args != null) {
            String announcementId = args.getString("announcementId");
            String title = args.getString("title");
            String description = args.getString("description");
            String date = args.getString("date");
            // Assume "announcementId" is a unique identifier for the announcement node

            ((TextView) view.findViewById(R.id.AnnouncementTitle)).setText(title);
            ((TextView) view.findViewById(R.id.AnnouncementDescription)).setText(description);
            ((TextView) view.findViewById(R.id.AnnouncementDate)).setText("Posted on" + date);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference announcementsRef = database.getReference("Announcements").child(announcementId);

            announcementsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String title = dataSnapshot.child("title").getValue(String.class);
                        String description = dataSnapshot.child("description").getValue(String.class);
                        String date = dataSnapshot.child("date").getValue(String.class);

                        // Update the UI with the retrieved announcement data
                        ((TextView) view.findViewById(R.id.AnnouncementTitle)).setText(title);
                        ((TextView) view.findViewById(R.id.AnnouncementDescription)).setText(description);
                        ((TextView) view.findViewById(R.id.AnnouncementDate)).setText("Sent On: " + date);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
        return view;
    }
}