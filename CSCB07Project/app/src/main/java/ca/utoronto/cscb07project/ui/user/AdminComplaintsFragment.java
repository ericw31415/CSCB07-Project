package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.complaints.complaints;
import ca.utoronto.cscb07project.ui.user.ComplaintAdapter;

public class AdminComplaintsFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ComplaintAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_complaints, container, false);

        ListView listView = view.findViewById(R.id.listView);
        List<complaints> complaints = new ArrayList<>();
        adapter = new ComplaintAdapter(requireContext(), complaints);
        listView.setAdapter(adapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Complaints");

        // Read data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaints.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    complaints complaint = dataSnapshot.getValue(complaints.class);
                    if (complaint != null) {
                        complaints.add(complaint);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });

        return view;
    }
}