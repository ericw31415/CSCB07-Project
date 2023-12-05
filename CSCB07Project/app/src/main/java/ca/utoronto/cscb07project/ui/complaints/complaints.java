package ca.utoronto.cscb07project.ui.complaints;

public class complaints {
    private String complaintId;
    private String title;
    private String userEmail;
    private String date;
    private String details;

    public complaints(){

    }
    public complaints(String complaintId, String title, String userEmail,
                      String date, String details) {
        this.complaintId = complaintId;
        this.title = title;
        this.userEmail = userEmail;
        this.date = date;
        this.details = details;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details){
        this.details = details;
    }
}
