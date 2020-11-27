package main.model;

import java.time.LocalTime;

public class Attendance {
    private byte grade;
    private String etrCode;
    private short year;
    private byte semester;
    private String day;
    private LocalTime begin;
    private String buildingCode;
    private String roomCode;

    public Attendance() {}

    public Attendance(String etrCode, short year, byte semester, String day, LocalTime begin, String buildingCode,
                      String roomCode) {
        this.etrCode = etrCode;
        this.year = year;
        this.semester = semester;
        this.day = day;
        this.begin = begin;
        this.buildingCode = buildingCode;
        this.roomCode = roomCode;
    }

    public Attendance(byte grade, String etrCode, short year, byte semester, String day, LocalTime begin,
                      String buildingCode, String roomCode) {
        this.grade = grade;
        this.etrCode = etrCode;
        this.year = year;
        this.semester = semester;
        this.day = day;
        this.begin = begin;
        this.buildingCode = buildingCode;
        this.roomCode = roomCode;
    }

    public byte getGrade() {
        return grade;
    }

    public void setGrade(byte grade) {
        this.grade = grade;
    }

    public String getEtrCode() {
        return etrCode;
    }

    public void setEtrCode(String etrCode) {
        this.etrCode = etrCode;
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
