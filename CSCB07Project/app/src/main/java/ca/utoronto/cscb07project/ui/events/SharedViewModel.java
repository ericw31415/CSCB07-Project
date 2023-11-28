package ca.utoronto.cscb07project.ui.events;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> allEventsList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> attendingEventsList = new MutableLiveData<>(new ArrayList<>());
    FirebaseApp secondaryApp = FirebaseApp.getInstance("secondary");
    FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(secondaryApp);
    DatabaseReference eventsRef = secondaryDatabase.getReference("events");

    public SharedViewModel() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        events.add(event);
                    }
                }
                setAllEventsList(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SharedViewModel", "Error fetching events: " + databaseError.getMessage());
            }
        });
    }

    public LiveData<List<Event>> getAllEventsList() {
        return allEventsList;
    }

    public LiveData<List<Event>> getAttendingEventsList() {
        return attendingEventsList;
    }

    public void addEvent(Event event) {
        List<Event> currentAllEvents = allEventsList.getValue();
        currentAllEvents.add(event);
        allEventsList.setValue(currentAllEvents);
    }

    public void rsvpEvent(Event event) {
        List<Event> currentAllEvents = allEventsList.getValue();
        currentAllEvents.remove(event);
        allEventsList.setValue(currentAllEvents);

        List<Event> currentAttendingEvents = attendingEventsList.getValue();
        currentAttendingEvents.add(event);
        attendingEventsList.setValue(currentAttendingEvents);
    }

    public void setAllEventsList(List<Event> events) {
        allEventsList.setValue(events);
    }
}


