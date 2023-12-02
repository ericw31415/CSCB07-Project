package ca.utoronto.cscb07project.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.events.Event;
import ca.utoronto.cscb07project.events.EventDetailsStudent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminEventsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminEventsList extends Fragment {

    private ListView listView;
    private ArrayAdapter<Event> adapter;
    private List<Event> events;
    private DatabaseReference eventsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        setupListView(view);
        return view;
    }

    private void setupListView(View view) {
        listView = view.findViewById(R.id.listView500);
        events = new ArrayList<>();

        adapter = new ArrayAdapter<Event>(getContext(), R.layout.event_item, events) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View itemView = convertView;
                if (itemView == null) {
                    itemView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
                }

                Event event = getItem(position);
                if (event != null) {
                    TextView titleTextView = itemView.findViewById(R.id.titleTextView);
                    TextView locationTextView = itemView.findViewById(R.id.locationTextView);
                    TextView dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);

                    titleTextView.setText(event.getTitle());
                    locationTextView.setText(event.getLocation());
                    dateTimeTextView.setText(event.getDateTime());
                }

                return itemView;
            }
        };

        listView.setAdapter(adapter);

        /**
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click, e.g., open detail fragment
                Event event = events.get(position);
                openDetailFragment(event);
            }
        });
        */

        // Initialize Realtime Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        eventsRef = database.getReference("Events");

        // Fetch events from Realtime Database
        fetchEventsFromFirebase();
    }

    private void fetchEventsFromFirebase() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    events.add(event);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void openDetailFragment(Event event) {
        EventDetailsStudent eventDetailsStudent = new EventDetailsStudent();
        Bundle args = new Bundle();
        args.putString("eventId", event.getId());
        eventDetailsStudent.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.userFrame, eventDetailsStudent);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}