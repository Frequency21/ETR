package main.model;

public class Course {
    private String courseCode;
    private short credit;
    private boolean pracitce;
    private String name;
    private String etrCode;

    public Course(String courseCode, short credit, boolean pracitce, String name, String etrCode) {
        this.courseCode = courseCode;
        this.credit = credit;
        this.pracitce = pracitce;
        this.name = name;
        this.etrCode = etrCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public short getCredit() {
        return credit;
    }

    public void setCredit(short credit) {
        this.credit = credit;
    }

    public boolean isPracitce() {
        return pracitce;
    }

    public void setPracitce(boolean pracitce) {
        this.pracitce = pracitce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEtrCode() {
        return etrCode;
    }

    public void setEtrCode(String etrCode) {
        this.etrCode = etrCode;
    }
}
