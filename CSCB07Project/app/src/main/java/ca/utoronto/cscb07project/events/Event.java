package ca.utoronto.cscb07project.events;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int participantLimit;
    private int participantCount;
    private String id;
    private String title;
    private String location;
    private String dateTime;
    private String description;
    private List<String> rsvps;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String id, String title, String dateTime, String location, String description, int participantLimit) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
        this.participantLimit = participantLimit; // setting participantLimit
        this.participantCount = 0; // initialize participantCount to 0
        this.rsvps = new ArrayList<>(); // initialize rsvps list
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
    public int getParticipantLimit() {
        return participantLimit;
    }

    public void setParticipantLimit(int participantLimit) {
        this.participantLimit = participantLimit;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
    public boolean addRSVP(String userEmail) {
        if (rsvps == null) {
            rsvps = new ArrayList<>();
        }

        if (rsvps.size() < participantLimit) {
            rsvps.add(userEmail);
            participantCount = rsvps.size();

            // Update both participantCount and rsvps on Firebase
            updateEventOnFirebase();

            return true;
        } else {
            return false;
        }
    }


    public static String generateUniqueId() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        return databaseReference.push().getKey();
    }
    public boolean removeRSVP(String userEmail) {
        if (rsvps != null && rsvps.contains(userEmail)) {
            rsvps.remove(userEmail);
            participantCount = rsvps.size();

            // Update both participantCount and rsvps on Firebase
            updateEventOnFirebase();

            return true;
        } else {
            return false;
        }
    }

    private void updateEventOnFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(id);
        databaseReference.setValue(this);
    }

    private void updateParticipantCountOnFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(id).child("participantCount");
        databaseReference.setValue(participantCount);
    }
}

