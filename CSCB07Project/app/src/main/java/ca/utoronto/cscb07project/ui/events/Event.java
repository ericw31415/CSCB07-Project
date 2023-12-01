package ca.utoronto.cscb07project.ui.events;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;
public class Event implements Serializable {

    private String title;
    private Calendar date;
    private List<String> usersAttending;
    private int maxUsers; // New field

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
