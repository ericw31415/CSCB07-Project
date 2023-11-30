package ca.utoronto.cscb07project.data;

public class User {
    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public boolean isAdmin;

    public User(String email, String firstName, String lastName, String password, boolean isAdmin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
