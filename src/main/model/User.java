package main.model;

public class User {
    private String etrCode;
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

    public User(String etrCode, String firstName, String lastName, String email) {
        this.etrCode = etrCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEtrCode() {
        return etrCode;
    }

    public void setEtrCode(String etrCode) {
        this.etrCode = etrCode;
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
