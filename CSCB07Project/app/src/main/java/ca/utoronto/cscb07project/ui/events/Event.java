package ca.utoronto.cscb07project.ui.events;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;
public class Event implements Serializable {

    private String title;
    private Calendar date;
    private List<String> usersAttending;
    private int maxUsers;
    private String eventId;


    public Event() {
    }

    public Event(String title, Calendar date, int maxUsers) {
        this.title = title;
        this.date = date;
        this.maxUsers = maxUsers;
    }
    public Event(String title) {
        this.title = title;
        // Initialize other fields as needed
    }
    public Event(String title, Calendar date) {
        this.title = title;
        this.date = date;
        this.maxUsers = 0; // Default value for maxUsers
        this.usersAttending = new ArrayList<>();
    }
    public Event(String eventId, String title, Calendar date, int maxUsers) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.maxUsers = maxUsers;
        this.usersAttending = new ArrayList<>();
    }
    public Calendar getDate() {
        return date;
    }
    public int getMaxUsers() {
        return maxUsers;
    }
    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public String getEventId() {
        return this.eventId;
    }
    public String getId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setUsersAttending(List<String> usersAttending) {
        this.usersAttending = usersAttending;
    }





    public List<String> getUsersAttending() {
        return usersAttending;
    }

    public void addUserAttending(String user) {
        this.usersAttending.add(user);
    }
}
