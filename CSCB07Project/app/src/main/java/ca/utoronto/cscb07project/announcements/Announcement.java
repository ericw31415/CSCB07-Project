package ca.utoronto.cscb07project.announcements;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Announcement implements Serializable {
    private String title;
    private String announcementID; // Update the variable name
    private String date;
    private String description;

    public Announcement(String title, String date, String description, String announcementID) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.announcementID = announcementID; // Update the variable name
    }

    public Announcement() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnouncementID() { // Update the method name
        return announcementID; // Update the variable name
    }

    public void setAnnouncementID(String announcementID) { // Update the method name
        this.announcementID = announcementID; // Update the variable name
    }

    public static String generateUniqueID() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcements");
        return databaseReference.push().getKey();
    }
}