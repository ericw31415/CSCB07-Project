package ca.utoronto.cscb07project.ui.events;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ca.utoronto.cscb07project.ui.signup.UserModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> allEventsList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> attendingEventsList = new MutableLiveData<>(new ArrayList<>());
    FirebaseOptions options = new FirebaseOptions.Builder()
            .setApplicationId("1:568217251391:android:25b9a28033be47e2e8528a") // Required for Analytics.
            .setApiKey("AIzaSyCp9Pgw40WZbdaBZSVvyLnWhjrt-_ImNHE") // Required for Auth.
            .setDatabaseUrl("https://cscb07-group-default-rtdb.firebaseio.com/") // Required for RTDB.
            .build();
    FirebaseApp secondaryApp = FirebaseApp.getInstance("secondary");
    FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(secondaryApp);
    DatabaseReference eventsRef = secondaryDatabase.getReference("events");
    DatabaseReference usersRef = secondaryDatabase.getReference("users");
    DatabaseReference rsvpRef = secondaryDatabase.getReference("rsvp");



    ;

    public SharedViewModel() {


        // Get a reference to the new database
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = new Event();
                    event.setTitle(eventSnapshot.child("title").getValue(String.class));
                    String dateString = eventSnapshot.child("date").getValue(String.class);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(format.parse(dateString));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    event.setDate(date);
                    // Add more fields if needed
                    events.add(event);
                }
                setAllEventsList(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SharedViewModel", "Error fetching events: " + databaseError.getMessage());
            }
        });
    }
    public LiveData<UserModel> getUser(String userId) {
        MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                userLiveData.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SharedViewModel", "Error fetching user: " + databaseError.getMessage());
            }
        });
        return userLiveData;
    }
    public LiveData<List<Event>> getAllEventsList() {
        return allEventsList;
    }

    public LiveData<List<Event>> getAttendingEventsList() {
        return attendingEventsList;
    }

    public LiveData<List<Event>> getAttendingEvents(String userEmail) {
        MutableLiveData<List<Event>> attendingEvents = new MutableLiveData<>(new ArrayList<>());
        rsvpRef.orderByChild("studentEmail").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();
                for (DataSnapshot rsvpSnapshot : dataSnapshot.getChildren()) {
                    String title = rsvpSnapshot.child("title").getValue(String.class);
                    Event event = new Event(title, null); // Replace null with the actual date if needed
                    events.add(event);
                }
                attendingEvents.setValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SharedViewModel", "Error getting attending events", databaseError.toException());
            }
        });
        return attendingEvents;
    }

    public void addEvent(Event event) {
        List<Event> currentAllEvents = allEventsList.getValue();
        currentAllEvents.add(event);
        allEventsList.setValue(currentAllEvents);

        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("title", event.getTitle());
        eventMap.put("date", event.getDate().getTimeInMillis());
        // Add more fields if needed
        eventsRef.push().setValue(eventMap);
    }

    public void rsvpEvent(Event event, String userEmail) {
        String key = rsvpRef.push().getKey();
        Map<String, Object> rsvp = new HashMap<>();
        rsvp.put("title", event.getTitle());
        rsvp.put("studentEmail", userEmail);
        rsvpRef.child(key).setValue(rsvp);
    }

    public void setAllEventsList(List<Event> events) {
        allEventsList.setValue(events);
    }
}


