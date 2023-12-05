package ca.utoronto.cscb07project.events;

public class RSVP {

    private String eventID;
    private String email;

    public RSVP() {
    }

    public RSVP(String eventID, String email) {
        this.eventID = eventID;
        this.email = email;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

