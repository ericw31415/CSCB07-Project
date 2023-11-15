package ca.utoronto.cscb07project.ui.loginsignout;

public class UserModel {
    private String userID;
    private String email;
    private String password;
    private String fName;
    private String lName;
    private boolean isAdmin;

    public UserModel(String userID, String email, String password, boolean isAdmin,
                     String fName, String lName) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.fName = fName;
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID(){
        return userID;
    }

    public String getPassword(){
        return password;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return fName + " " + lName + ", " + userID;
    }

}
