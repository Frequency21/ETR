package main.dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.scene.control.Alert;
import main.model.Lecture;
import main.model.Teacher;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO extends DAO {

    public List<Lecture> getLectures() {
        String sql = "SELECT * FROM tanora";
        return getLectures(sql);
    }

    @SuppressWarnings("DuplicatedCode")
    public void addLecture(short year, byte semester, String day, LocalTime begin, LocalTime end, short max, String courseCode,
                           String buildingCode, String roomCode)
        throws SQLIntegrityConstraintViolationException {
        String sql = "INSERT INTO tanora VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setShort(1, year);
            stmt.setByte(2, semester);
            stmt.setString(3, day);
            stmt.setTime(4, Time.valueOf(begin));
            stmt.setTime(5, Time.valueOf(end));
            stmt.setShort(6, max);
            stmt.setString(7, courseCode);
            stmt.setString(8, buildingCode);
            stmt.setString(9, roomCode);
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw ex;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteLecture(short year, byte semester, String day, LocalTime begin, String buildingCode, String roomCode) {
        String sql = "DELETE FROM tanora WHERE ev = ? and felev = ? and nap = ? and kezdes = ? and epulet_kod = ? " +
            "and terem_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setShort(1, year);
            stmt.setByte(2, semester);
            stmt.setString(3, day);
            stmt.setTime(4, Time.valueOf(begin));
            stmt.setString(5, buildingCode);
            stmt.setString(6, roomCode);
            int rowDeleted = stmt.executeUpdate();
            Alert alert;
            if (rowDeleted == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A törlés sikertelen volt, nincs ilyen tanóra.");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A törlés sikeres volt.");
            }
            alert.setTitle("Törlés");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
//  PRIMARY KEY (ev, felev, nap, kezdes, epulet_kod, terem_kod),
    @SuppressWarnings("DuplicatedCode")
    public void updateLecture(short year, byte semester, String day, LocalTime begin, LocalTime end, short max,
                              String courseCode, String buildingCode, String roomCode) {
        String sqlUpd = "UPDATE tanora SET ev = ?, felev = ?, nap = ?, kezdes = ?, vegzes = ?, max_letszam = ?, " +
            "kurzus_kod = ?, epulet_kod = ?, terem_kod = ? WHERE ev = ? and felev = ? and nap = ? and kezdes = ? and " +
            "epulet_kod = ? and terem_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlUpd)) {
            stmt.setShort(1, year);
            stmt.setByte(2, semester);
            stmt.setString(3, day);
            stmt.setTime(4, Time.valueOf(begin));
            stmt.setTime(5, Time.valueOf(end));
            stmt.setShort(6, max);
            stmt.setString(7, courseCode);
            stmt.setString(8, buildingCode);
            stmt.setString(9, roomCode);
            stmt.setShort(10, year);
            stmt.setByte(11, semester);
            stmt.setString(12, day);
            stmt.setTime(13, Time.valueOf(begin));
            stmt.setString(14, buildingCode);
            stmt.setString(15, roomCode);
            stmt.executeUpdate();
            int rowUpd = stmt.executeUpdate();
            Alert alert;
            if (rowUpd == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A frissítés sikertelen volt, nincs ilyen tanóra.");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A frissítés sikeres volt.");
            }
            alert.setTitle("Frissítés");
            alert.show();
        } catch (MysqlDataTruncation ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen frissítés");
            alert.setHeaderText("Az egyik frissítendő adat túl hosszú.");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<Lecture> getLectures(String sql) {
        List<Lecture> lectures = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                lectures.add(createLecture(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lectures;
    }

    private Lecture createLecture(ResultSet rs) {
        Lecture lecture = null;
        try {
            lecture = new Lecture(
                rs.getShort("ev"),
                rs.getByte("felev"),
                rs.getString("nap"),
                rs.getTime("kezdes").toLocalTime(),
                rs.getTime("vegzes").toLocalTime(),
                rs.getShort("max_letszam"),
                rs.getString("kurzus_kod"),
                rs.getString("epulet_kod"),
                rs.getString("terem_kod")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lecture;
    }

}
