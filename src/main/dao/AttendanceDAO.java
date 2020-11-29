package main.dao;

import javafx.scene.control.Alert;
import main.exceptions.MissingRequirementException;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO extends DAO {

    @SuppressWarnings("DuplicatedCode")
    public void addAttendanceWithGrade(byte grade, String etrCode, short year, byte semester, String day,
                                       LocalTime begin, String buildingCode, String roomCode)
        throws MissingRequirementException {
        List<String> missingCourseCodes = missingRequirements(etrCode, year, semester, day, begin);
        if (!missingCourseCodes.isEmpty())
            throw new MissingRequirementException(missingCourseCodes);
        String sql = "INSERT INTO felvett VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setByte(1, grade);
            stmt.setString(2, etrCode);
            stmt.setShort(3, year);
            stmt.setByte(4, semester);
            stmt.setString(5, day);
            stmt.setTime(6, Time.valueOf(begin));
            stmt.setString(7, buildingCode);
            stmt.setString(8, roomCode);
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("A teljesítés felvétele sikertelen volt!");
            if (ex.getMessage().contains("PRIMARY")) {
                alert.setHeaderText("Ezt a tanórát az adott hallgató már felvette / teljesítette!");
            }
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @SuppressWarnings("DuplicatedCode")
    public void addAttendance(String etrCode, short year, byte semester, String day,
                              LocalTime begin, String buildingCode, String roomCode)
        throws MissingRequirementException {
        List<String> missingCourseCodes = missingRequirements(etrCode, year, semester, day, begin);
        if (!missingCourseCodes.isEmpty())
            throw new MissingRequirementException(missingCourseCodes);
        String sql = "INSERT INTO felvett VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setNull(1, Types.TINYINT);
            stmt.setString(2, etrCode);
            stmt.setShort(3, year);
            stmt.setByte(4, semester);
            stmt.setString(5, day);
            stmt.setTime(6, Time.valueOf(begin));
            stmt.setString(7, buildingCode);
            stmt.setString(8, roomCode);
            int success = stmt.executeUpdate();
            Alert alert;
            if (success > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tanóra felvétele");
                alert.setHeaderText("A tanóra felvétele sikeres volt.");
            } else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Tanóra felvétele");
                alert.setHeaderText("A tanóra felvétele sikertelen volt.");
            }
            alert.show();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("A tanóra felvétele sikertelen volt!");
            if (ex.getMessage().contains("fk_felvett_tanora")) {
                alert.setHeaderText("Ezt a tanórát az adott hallgató már felvette / teljesítette!");
            }
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<String> missingRequirements(String etrCode, short year, byte semester, String day, LocalTime begin) {
        List<String> missingCoursesCode = new ArrayList<>();
        String sqlFindCourseCode = "SELECT kurzus_kod FROM tanora WHERE ev = ? AND felev = ? AND nap = ? AND kezdes = ?";
        String courseCode = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlFindCourseCode)) {
            stmt.setShort(1, year);
            stmt.setByte(2, semester);
            stmt.setString(3, day);
            stmt.setTime(4, Time.valueOf(begin));
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                courseCode = rs.getString("kurzus_kod");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (courseCode == null)
            throw new RuntimeException("courseCode is null.");
        String sqlFindRequirements = "SELECT kurzus_kod_felt FROM elofeltetele WHERE kurzus_kod = ? AND " +
            "kurzus_kod_felt NOT IN (SELECT kurzus_kod FROM felvett natural join tanora WHERE etr_kod = ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlFindRequirements)) {
            stmt.setString(1, courseCode);
            stmt.setString(2, etrCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                missingCoursesCode.add(rs.getString("kurzus_kod_felt"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return missingCoursesCode;
    }

    public void deleteAttendance(String etrCode, short year, byte semester, String day, LocalTime begin) {
        String sql = "DELETE FROM felvett WHERE etr_kod = ? AND ev = ? AND felev = ? AND nap = ? AND kezdes = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, etrCode);
            stmt.setShort(2, year);
            stmt.setByte(3, semester);
            stmt.setString(4, day);
            stmt.setTime(5, Time.valueOf(begin));
            int rowDeleted = stmt.executeUpdate();
            Alert alert;
            if (rowDeleted == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A törlés sikertelen volt, nincs ilyen teljesítés.");
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

    @SuppressWarnings("DuplicatedCode")
    public void updateAttendanceWithGrade(byte grade, String etrCode, short year, byte semester, String day, LocalTime begin) {
        String sqlUpd = "UPDATE felvett SET erdemjegy = ? WHERE etr_kod = ? AND ev = ? AND felev = ? AND nap = ? AND kezdes = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlUpd)) {
            stmt.setByte(1, grade);
            stmt.setString(2, etrCode);
            stmt.setShort(3, year);
            stmt.setByte(4, semester);
            stmt.setString(5, day);
            stmt.setTime(6, Time.valueOf(begin));
            int rowUpd = stmt.executeUpdate();
            Alert alert;
            if (rowUpd == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A frissítés sikertelen volt, nincs ilyen teljesítés.");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A frissítés sikeres volt.");
            }
            alert.setTitle("Frissítés");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void updateAttendance(String etrCode, short year, byte semester, String day, LocalTime begin) {
        String sqlUpd = "UPDATE felvett SET erdemjegy = ? WHERE etr_kod = ? AND ev = ? AND felev = ? AND nap = ? AND kezdes = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlUpd)) {
            stmt.setNull(1, Types.TINYINT);
            stmt.setString(2, etrCode);
            stmt.setShort(3, year);
            stmt.setByte(4, semester);
            stmt.setString(5, day);
            stmt.setTime(6, Time.valueOf(begin));
            int rowUpd = stmt.executeUpdate();
            Alert alert;
            if (rowUpd == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A frissítés sikertelen volt, nincs ilyen teljesítés.");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A frissítés sikeres volt.");
            }
            alert.setTitle("Frissítés");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
