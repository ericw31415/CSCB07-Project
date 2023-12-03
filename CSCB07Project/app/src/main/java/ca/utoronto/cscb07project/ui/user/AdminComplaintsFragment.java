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
import ca.utoronto.cscb07project.ui.complaints.complaints;

public class AdminComplaintsFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ComplaintAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_complaints, container, false);

        ListView listView = view.findViewById(R.id.listView500);
        List<complaints> complaints = new ArrayList<>();
        adapter = new ComplaintAdapter(requireContext(), complaints);
        listView.setAdapter(adapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("complaints");

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

                Collections.reverse(complaints);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });


        adapter.setOnComplaintClickListener(new ComplaintAdapter.OnComplaintClickListener() {
            @Override
            public void onComplaintClick(ca.utoronto.cscb07project.ui.complaints.complaints complaint) {
                toComplaintDetailsFragment(complaint);
            }
        });

        return view;
    }

    private void toComplaintDetailsFragment(complaints complaint){
        ComplaintDetailsFragment complaintDetailsFragment = new ComplaintDetailsFragment();
        Bundle args = new Bundle();
        args.putString("complaintID", complaint.getComplaintId());
        args.putString("studentEmail", complaint.getUserEmail());
        args.putString("title", complaint.getTitle());
        args.putString("details", complaint.getDetails());
        args.putString("date", complaint.getDate());

        complaintDetailsFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, complaintDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}