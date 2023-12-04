package ca.utoronto.cscb07project.announcements;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;

public class All_Announcement_Fragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Announcement> adapter;
    private List<Announcement> announcements;
    private DatabaseReference announcementsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all__announcement_, container, false);
        setupListView(view);
        return view;
    }

    private void setupListView(View view) {
        listView = view.findViewById(R.id.listViewAnnouncement);
        announcements = new ArrayList<>();

        adapter = new ArrayAdapter<Announcement>(getContext(), R.layout.announcement_item, announcements) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View itemView = convertView;
                if (itemView == null) {
                    itemView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_item, parent, false);
                }

                Announcement announcement = getItem(position);
                if (announcement != null) {
                    TextView titleTextView = itemView.findViewById(R.id.textViewTitleAnnouncement);
                    TextView dateTimeTextView = itemView.findViewById(R.id.textViewDateAnnouncement);
                    TextView descriptionTimeTextView = itemView.findViewById(R.id.textViewAnnouncementId);

                    titleTextView.setText(announcement.getTitle());
                    dateTimeTextView.setText(announcement.getDate());
                    descriptionTimeTextView.setText(announcement.getDescription());
                }

                return itemView;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Announcement announcement = announcements.get(position);
                openDetailFragment(announcement);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        announcementsRef = database.getReference("Announcements");

        fetchAnnouncementsFromFirebase();
    }

    private void fetchAnnouncementsFromFirebase() {
        announcementsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announcements.clear();

                for (DataSnapshot announcementsSnapshot : dataSnapshot.getChildren()) {
                    Announcement announcement = announcementsSnapshot.getValue(Announcement.class);

                    if (announcement != null) {
                        String eventID = announcement.getEventID();

                        if (eventID != null && eventID.equals("blank")) {
                            announcements.add(announcement);
                        } else {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (currentUser != null) {
                                String currentUserEmail = currentUser.getEmail();

                                if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
                                    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

                                    // Add a null check for eventID
                                    if (eventID != null) {
                                        DatabaseReference rsvpsRef = eventsRef.child(eventID).child("rsvps");

                                        rsvpsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                boolean userRSVPd = false;

                                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                    String email = childSnapshot.getValue(String.class);

                                                    // Check if the retrieved email matches the current user's email
                                                    if (email != null && email.equals(currentUserEmail)) {
                                                        userRSVPd = true;
                                                        Log.d("RSVP", "User has RSVP'd" + announcement.getAnnouncementID());
                                                        // No need to add to announcements here; we'll do that outside the loop
                                                        // Exit the loop early since you found a match
                                                        break;
                                                    }
                                                }

                                                // If the user has RSVP'd, add the announcement
                                                if (userRSVPd) {
                                                    announcements.add(announcement);
                                                }

                                                // Notify the adapter here after determining RSVP status
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle any errors that may occur
                                                databaseError.toException().printStackTrace();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }

                // Notify the adapter here after checking all announcements
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void openDetailFragment(Announcement announcement) {
        Toast.makeText(getContext(), announcement.getAnnouncementID(), Toast.LENGTH_SHORT).show();
        Announcement_DetailFragment announcementDetailFragment = new Announcement_DetailFragment();
        Bundle args = new Bundle();
        args.putString("announcementID", announcement.getAnnouncementID());
        announcementDetailFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, announcementDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}