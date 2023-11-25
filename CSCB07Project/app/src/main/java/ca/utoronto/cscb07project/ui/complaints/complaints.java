package ca.utoronto.cscb07project.ui.complaints;

public class complaints  {
    private String complaintId;
    private String title;
    private String details;

    // Constructor
    public complaints(String complaintId, String title, String details) {
        this.complaintId = complaintId;
        this.title = title;
        this.details = details;
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
}