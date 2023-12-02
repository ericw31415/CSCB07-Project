package ca.utoronto.cscb07project.events2;

public class Event{

    private String title;
    private String location;
    private String dateTime;
    private String description;

    public Event(String title, String dateTime, String location, String description) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
    }

    // Getter and Setter methods for 'title'
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    // Getter and Setter methods for 'location'
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    // Getter and Setter methods for 'description'
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
