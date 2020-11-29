package main.dao;

import main.model.LectureRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureRoomDAO extends DAO {

    public List<LectureRoom> getLectureRooms() {
        List<LectureRoom> lectureRooms = new ArrayList<>();
        String sql = "SELECT * FROM terem";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                lectureRooms.add(createLectureRoom(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lectureRooms;
    }

    /*public void addLectureRoom(LectureRoom lectureRoom)
        throws SQLIntegrityConstraintViolationException {
        String sql = "INSERT INTO terem VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtUser = connection.prepareStatement(sql)) {
            stmtUser.setString(1, lectureRoom.getBuildingCode());
            stmtUser.setString(2, lectureRoom.getRoomCode());
            stmtUser.setBoolean(3, lectureRoom.isCabinet());
            stmtUser.setShort(4, lectureRoom.getMax());
            stmtUser.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Terem felvétele");
            alert.setHeaderText("A terem már szerepel a rendszerben!");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteLectureRoom(LectureRoom lectureRoom) {
        String sql = "DELETE FROM terem WHERE epulet_kod = ? and terem_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lectureRoom.getBuildingCode());
            stmt.setString(2, lectureRoom.getRoomCode());
            int rowDeleted = stmt.executeUpdate();
            Alert alert;
            if (rowDeleted == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A törlés sikertelen volt, nincs ilyen kurzus.");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A törlés sikeres volt.");
            }
            alert.setTitle("Törlés");
            alert.show();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen törlés!");
            alert.setHeaderText("A terem törlése nem lehetséges. (Valószínűleg van benne tanóra meg tartva.)");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateLectureRoom() {

    }*/

    private LectureRoom createLectureRoom(ResultSet rs) {
        LectureRoom lectureRoom = null;
        try {
            lectureRoom = new LectureRoom(
                rs.getString("epulet_kod"),
                rs.getString("terem_kod"),
                rs.getBoolean("kabinet"),
                rs.getShort("max_letszam")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lectureRoom;
    }

}
