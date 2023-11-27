package ca.utoronto.cscb07project.ui.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;

public class ComplaintDataViewModel extends ViewModel {
    private MutableLiveData<String> complaintId = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();
    private MutableLiveData<String> details = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> userEmail = new MutableLiveData<>();

    public void setComplaintInfo(String complaintId, String date, String details, String title, String userEmail){
        this.complaintId.setValue(complaintId);
        this.date.setValue(date);
        this.details.setValue(details);
        this.title.setValue(title);
        this.userEmail.setValue(userEmail);
    }

    public LiveData<String> getComplaintId() {
        return complaintId;
    }

    public LiveData<String> getDate() {
        return date;
    }

    public LiveData<String> getDetails() {
        return details;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }
}


