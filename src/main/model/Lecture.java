package main.model;

import java.time.LocalTime;

public class Lecture {
    private short year;
    private byte semester;
    private String day;
    private LocalTime begin, end;
    private short max;
    private String courseCode;
    private String buildingCode;
    private String roomCode;

    public Lecture(short year, byte semester, String day, LocalTime begin, LocalTime end, short max,
                   String courseCode, String buildingCode, String roomCode) {
        this.year = year;
        this.semester = semester;
        this.day = day;
        this.begin = begin;
        this.end = end;
        this.max = max;
        this.courseCode = courseCode;
        this.buildingCode = buildingCode;
        this.roomCode = roomCode;
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

    public short getMax() {
        return max;
    }

    public void setMax(short max) {
        this.max = max;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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
