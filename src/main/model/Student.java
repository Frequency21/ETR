package main.model;

public class Student {
    private String etrCode;
    private String firstName;
    private String lastName;
    private String major;
    private Short year;
    private String email;

    public Student() {
    }

    public Student(String firstName, String lastName, String major, Short year, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.year = year;
        this.email = email;
    }

    public Student(String etrCode, String firstName, String lastName, String major, Short year, String email) {
        this.etrCode = etrCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.year = year;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
            "etrCode='" + etrCode + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", major='" + major + '\'' +
            ", year=" + year +
            ", email='" + email + '\'' +
            '}';
    }
}
