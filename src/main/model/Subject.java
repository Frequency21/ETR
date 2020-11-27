package main.model;

public class Subject {
    private String courseCode;
    private String name;
    private short credit;
    private byte grade;
    private short year;
    private byte semester;

    public Subject() {}

    public Subject(String courseCode, String name, short credit, byte grade, short year, byte semester) {
        this.courseCode = courseCode;
        this.name = name;
        this.credit = credit;
        this.grade = grade;
        this.year = year;
        this.semester = semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getCredit() {
        return credit;
    }

    public void setCredit(short credit) {
        this.credit = credit;
    }

    public byte getGrade() {
        return grade;
    }

    public void setGrade(byte grade) {
        this.grade = grade;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public byte getSemester() {
        return semester;
    }

    public void setSemester(byte semester) {
        this.semester = semester;
    }
}
