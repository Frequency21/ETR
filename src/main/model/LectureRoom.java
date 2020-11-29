package main.model;

public class LectureRoom {
    private String buildingCode;
    private String roomCode;
    private boolean cabinet;
    private Short max;

    public LectureRoom(String buildingCode, String roomCode, boolean cabinet, Short max) {
        this.buildingCode = buildingCode;
        this.roomCode = roomCode;
        this.cabinet = cabinet;
        this.max = max;
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

    public boolean isCabinet() {
        return cabinet;
    }

    public void setCabinet(boolean cabinet) {
        this.cabinet = cabinet;
    }

    public Short getMax() {
        return max;
    }

    public void setMax(Short max) {
        this.max = max;
    }
}
