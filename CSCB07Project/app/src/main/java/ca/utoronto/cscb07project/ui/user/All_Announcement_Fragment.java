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
        Announcement_DetailFragment fragment = new Announcement_DetailFragment();
        Bundle args = new Bundle();
        args.putString("announcementID", announcement.getAnnouncementId());
        args.putString("title", announcement.getTitle());
        args.putString("description", announcement.getDescription());
        args.putString("date", announcement.getDate());

        fragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link AdminComplaintsFragment.Add_announcementFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class Add_announcementFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public Add_announcementFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Add_announcementFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static AdminComplaintsFragment.Add_announcementFragment newInstance(String param1, String param2) {
            AdminComplaintsFragment.Add_announcementFragment fragment = new AdminComplaintsFragment.Add_announcementFragment();
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
            return inflater.inflate(R.layout.fragment_admin_complaints, container, false);
        }
    }
}