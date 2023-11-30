package ca.utoronto.cscb07project.feedback;

import com.google.firebase.database.DatabaseReference;

public class Feedback {

    public DatabaseReference userId;
    public String event;
    public float rating;
    public String feedback;

    public Feedback(DatabaseReference userId, String event, float rating, String feedback){
        this.userId = userId;
        this.event = event;
        this.rating = rating;
        this.feedback = feedback;
    }
}
