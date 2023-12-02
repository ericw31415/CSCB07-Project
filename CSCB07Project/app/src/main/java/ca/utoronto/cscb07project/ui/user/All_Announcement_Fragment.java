package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.utoronto.cscb07project.R;

public class All_Announcement_Fragment extends Fragment {
    private DatabaseReference databaseReference;
    private AnnouncementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all__announcement_, container, false);

        ListView listView = view.findViewById(R.id.listViewAnnouncement);
        List<Announcement> announcements = new ArrayList<>();
        adapter = new AnnouncementAdapter(requireContext(), announcements);
        listView.setAdapter(adapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Announcements");

        // Read data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcements.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Announcement announcement = dataSnapshot.getValue(Announcement.class);
                    if (announcement != null) {
                        announcements.add(announcement);
                    }
                }

                Collections.reverse(announcements);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });

        adapter.setOnAnnouncementClickListener(new AnnouncementAdapter.OnAnnouncementClickListener()  {
            @Override
            public void onAnnouncementClick(ca.utoronto.cscb07project.ui.user.Announcement announcement) {
             toAnnouncmenetDetailsFragment(announcement);
            }
        });

        return view;
    }

    private void toAnnouncmenetDetailsFragment(ca.utoronto.cscb07project.ui.user.Announcement announcement) {
        // Implement logic to navigate to the Complaint Details Fragment
        // For example:
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Announcement_DetailFragment fragment = new Announcement_DetailFragment();
        // Pass data to ComplaintDetailsFragment if needed
        Bundle bundle = new Bundle();
        bundle.putString("AnnouncementId", announcement.getAnnouncementId());
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}