package ca.utoronto.cscb07project.announcements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

    private static final String ARG_ANNOUNCEMENT_ID = "announcementID";

    private String AnnouncementId;
    private TextView AnnouncementTitle;
    private TextView AnnouncementDescription;
    private TextView AnnouncementDate;

    public Announcement_DetailFragment() {

    }

    public static Announcement_DetailFragment newInstance(String announcementId) {
        Announcement_DetailFragment fragment = new Announcement_DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ANNOUNCEMENT_ID, announcementId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            AnnouncementId = getArguments().getString(ARG_ANNOUNCEMENT_ID);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement__detail, container, false);

        AnnouncementTitle = view.findViewById(R.id.AnnouncementTitle);
        AnnouncementDate = view.findViewById(R.id.AnnouncementDate);
        AnnouncementDescription = view.findViewById(R.id.AnnouncementDescription);

        if (getArguments() != null) {
            AnnouncementId = getArguments().getString(ARG_ANNOUNCEMENT_ID);
            Log.d("test", AnnouncementId.toString());
            DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Announcements").child(AnnouncementId);

            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String title = dataSnapshot.child("title").getValue(String.class);
                        String dateTime = dataSnapshot.child("date").getValue(String.class);
                        String description = dataSnapshot.child("description").getValue(String.class);

                        AnnouncementTitle.setText(title);
                        AnnouncementDate.setText(dateTime);
                        AnnouncementDescription.setText(description);
                    } else {
                        Log.d("Announcement_DetailFrag", "DataSnapshot does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Announcement_DetailFrag", "Error in database operation", error.toException());
                }
            });
        } else {
            // Handle the case where getArguments() is null
            Log.e("Announcement_DetailFrag", "Arguments are null");
        }

        return view;
    }
}
