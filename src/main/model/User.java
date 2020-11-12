package main.model;

public class User {
    private String etr_code;
    private String firstName;
    private String lastName;
    private String email;

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String etr_code, String firstName, String lastName, String email) {
        this.etr_code = etr_code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEtr_code() {
        return etr_code;
    }

    public void setEtr_code(String etr_code) {
        this.etr_code = etr_code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
