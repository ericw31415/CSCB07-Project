package ca.utoronto.cscb07project.announcements;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Announcement implements Serializable {
    private String title;
    private String announcementId; // Corrected spelling
    private String date;
    private String description;

    public Announcement(String title, String date, String description, String announcementId) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.announcementId = announcementId; // Corrected spelling
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

    public String getAnnouncementId() { // Corrected spelling
        return announcementId; // Corrected spelling
    }

    public void setAnnouncementId(String announcementId) { // Corrected spelling
        this.announcementId = announcementId; // Corrected spelling
    }
    public static String generateUniqueID() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcements");
        return databaseReference.push().getKey();
    }
}
