package ca.utoronto.cscb07project.data;

public class User {
    public String email;
    public String fName;
    public String lName;
    public String password;
    public boolean admin;

    public User(String email, String firstName, String lastName, String password, boolean isAdmin) {
        this.email = email;
        this.fName = firstName;
        this.lName = lastName;
        this.password = password;
        this.admin = isAdmin;
    }
}
