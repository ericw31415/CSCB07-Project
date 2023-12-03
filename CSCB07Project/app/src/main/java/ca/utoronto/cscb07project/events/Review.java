package ca.utoronto.cscb07project.events;

public class Review {
    private String userEmail;
    private String eventId;
    private String reviewText;
    private int rating;

    public Review() {
    }

    public Review(String userEmail, String eventId, String reviewText, int rating) {
        this.userEmail = userEmail;
        this.eventId = eventId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}


