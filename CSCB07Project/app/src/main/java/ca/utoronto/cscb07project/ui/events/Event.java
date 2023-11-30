package ca.utoronto.cscb07project.ui.events;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;
public class Event implements Serializable {

    private String title;
    private Calendar date;
    private List<String> usersAttending;

    public Event() {
    }

    public Event(String title, Calendar date) {
        this.title = title;
        this.date = date;
        this.usersAttending = new ArrayList<>();
    }

    public Calendar getDate() {
        return date;
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





    public List<String> getUsersAttending() {
        return usersAttending;
    }

    public void addUserAttending(String user) {
        this.usersAttending.add(user);
    }
}
