package main.model;

/*Oktató, vnév, knév, kurzus_kod, év, félév, kezdés, végzés, max_létszám, épület_kód, terem_kód*/

import java.time.LocalTime;

public class Teaches {
        private String etrCode;
        private String firstName;
        private String lastName;
        private String courseCode;
        private String courseName;
        private short year;
        private byte semester;
        private String day;
        private LocalTime begin;
        private LocalTime end;
        private String buildingCode;
        private String roomCode;

    public Teaches(String etrCode, String firstName, String lastName,
                   String courseCode, String courseName, short year,
                   byte semester, String day, LocalTime begin, LocalTime end,
                   String buildingCode, String roomCode) {
        this.etrCode = etrCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.year = year;
        this.semester = semester;
        this.day = day;
        this.begin = begin;
        this.end = end;
        this.buildingCode = buildingCode;
        this.roomCode = roomCode;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public void setBegin(LocalTime begin) {
        this.begin = begin;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}
