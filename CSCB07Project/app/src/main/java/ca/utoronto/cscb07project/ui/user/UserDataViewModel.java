package ca.utoronto.cscb07project.ui.user;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class UserDataViewModel extends ViewModel {
    private MutableLiveData<String> userEmail = new MutableLiveData<>();
    private MutableLiveData<String> firstName = new MutableLiveData<>();
    private MutableLiveData<String> lastName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAdmin = new MutableLiveData<>();

    public void setUserInfo(String firstName, String lastName, String userEmail, Boolean isAdmin) {
        this.firstName.setValue(firstName);
        this.lastName.setValue(lastName);
        this.userEmail.setValue(userEmail);
        this.isAdmin.setValue(isAdmin);
    }

    public MutableLiveData<String> getUserEmail() {
        return userEmail;
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public MutableLiveData<Boolean> getIsAdmin() {
        return isAdmin;
    }
}
