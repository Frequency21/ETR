package main.model;

/**
 * Melyik teremben hány órát tartanak az adott évben, félévben
 */
public class LectureRoomStatistic {
    private String buildingCode;
    private String roomCode;
    private boolean cabinet;
    private short max;
    private short year;
    private byte semester;
    private int quantity;

    public LectureRoomStatistic(String buildingCode, String roomCode, boolean cabinet, short max, short year, byte semester, int quantity) {
        this.buildingCode = buildingCode;
        this.roomCode = roomCode;
        this.cabinet = cabinet;
        this.max = max;
        this.year = year;
        this.semester = semester;
        this.quantity = quantity;
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

    public short getMax() {
        return max;
    }

    public void setMax(short max) {
        this.max = max;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
