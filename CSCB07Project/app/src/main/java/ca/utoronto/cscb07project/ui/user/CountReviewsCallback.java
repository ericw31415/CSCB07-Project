package ca.utoronto.cscb07project.ui.user;

public interface CountReviewsCallback{
    void onCountReceived(long count, double averageRating);
    void onCountError(String error);
}
