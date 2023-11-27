package ca.utoronto.cscb07project.ui.complaints;

public class complaints {
    private String complaintId;
    private String title;
    private String details;
    private String userEmail;
    private String date;

    public complaints(){

    }

    // Constructor
    public complaints(String complaintId, String title, String details, String userEmail,
                      String date) {
        this.complaintId = complaintId;
        this.title = title;
        this.details = details;
        this.userEmail = userEmail;
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    // Getters and setters for each field
    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
