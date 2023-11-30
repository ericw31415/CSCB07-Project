package ca.utoronto.cscb07project.ui.signup;

public class UserModel {
    private String email;
    private String password;
    private String fName;
    private String lName;
    private boolean isAdmin;

    public UserModel(String email, String password,
                     String fName, String lName, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.fName = fName;
        this.lName = lName;
    }

    public String getEmail() {
        return email;
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

    public void setPassword(String password) {
        this.password = password;
    }


}