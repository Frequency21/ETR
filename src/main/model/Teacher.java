package main.model;

public class Teacher extends User {
    private String department;

    public Teacher(String vnev, String knev, String email, String department) {
        super(vnev, knev, email);
        this.department = department;
    }

    public Teacher(String etr_kod, String vnev, String knev, String email, String department) {
        super(etr_kod, vnev, knev, email);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
