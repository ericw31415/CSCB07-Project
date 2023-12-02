package ca.utoronto.cscb07project.events;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String id;
    private String title;
    private String location;
    private String dateTime;
    private String description;
    private List<String> rsvps;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String id, String title, String dateTime, String location, String description) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
    }

    public void addRSVP(String userEmail) {
        if (rsvps == null) {
            rsvps = new ArrayList<>();
        }
        rsvps.add(userEmail);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title + " - " + location;
    }

    public List<String> getRsvps() {
        return rsvps;
    }

    public void setRsvps(List<String> rsvps) {
        this.rsvps = rsvps;
    }

    // Add this method to the Event class
    public boolean isUserRSVP(String userEmail) {
        return rsvps != null && rsvps.contains(userEmail);
    }


    public static String generateUniqueId() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        return databaseReference.push().getKey();
    }
}
