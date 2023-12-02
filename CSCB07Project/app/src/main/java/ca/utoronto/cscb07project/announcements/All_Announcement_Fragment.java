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
import ca.utoronto.cscb07project.announcements.Announcement;
import ca.utoronto.cscb07project.announcements.AnnouncementAdapter;
import ca.utoronto.cscb07project.announcements.Announcement_DetailFragment;

public class All_Announcement_Fragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Announcement> adapter;
    private List<Announcement> announcements;
    private DatabaseReference announcementsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_all__announcement_, container, false);
        setupListView(view);
        return view;
    }

    private void setupListView(View view){
        listView = view.findViewById(R.id.listView);
        announcements = new ArrayList<>();

        adapter = new ArrayAdapter<Announcement>(getContext(), R.layout.announcement_item, announcements){
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                View itemView = convertView;
                if (itemView == null){
                    itemView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_item, parent, false);
                }

                Announcement announcement = getItem(position);
                if(announcement != null){
                    TextView titleTextView = itemView.findViewById(R.id.AnnouncementTitle);
                    TextView dateTimeTextView = itemView.findViewById(R.id.AnnouncementDate);
                    TextView describtionTimeTextView = itemView.findViewById(R.id.AnnouncementDescription);

                    titleTextView.setText(announcement.getTitle());
                    dateTimeTextView.setText(announcement.getDate());
                    describtionTimeTextView.setText(announcement.getDescription());
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

                for(DataSnapshot annoucmentsSnapshot : dataSnapshot.getChildren()){
                    Announcement announcement = annoucmentsSnapshot.getValue(Announcement.class);
                    announcements.add(announcement);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void openDetailFragment(Announcement announcement){
        Announcement_DetailFragment announcementDetailFragment = new Announcement_DetailFragment();
        Bundle args = new Bundle();
        args.putString("announcementID", announcement.getAnnouncementId());
        announcementDetailFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, announcementDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
