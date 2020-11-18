package main.dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.scene.control.Alert;
import main.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DAO {

    public List<Course> getCourses() {
        String sql = "SELECT * FROM kurzus";
        return getCourses(sql);
    }

    public void addCourse(String courseCode, short credit, boolean practice, String name, String etrCode)
        throws SQLIntegrityConstraintViolationException {
        String sql = "INSERT INTO kurzus VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtUser = connection.prepareStatement(sql)) {
            stmtUser.setString(1, courseCode);
            stmtUser.setShort(2, credit);
            stmtUser.setBoolean(3, practice);
            stmtUser.setString(4, name);
            stmtUser.setString(5, etrCode);
            stmtUser.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw ex;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteCourse(String courseCode) {
        String sql = "DELETE FROM kurzus WHERE kurzus_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
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
            alert.setHeaderText("A kurzus törlése nem lehetséges. (Valószínűleg van belőle kurzus tartva.)");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateCourse(String courseCode, short credit, boolean practice, String name, String etrCode) {
        String sqlUpdate = "UPDATE kurzus SET kredit_ertek = ?, gyakorlat = ?, nev = ?, etr_kod = ? " +
            "WHERE kurzus_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
            stmt.setShort(1, credit);
            stmt.setBoolean(2, practice);
            stmt.setString(3, name);
            stmt.setString(4, etrCode);
            stmt.setString(5, courseCode);
            stmt.executeUpdate();
        } catch (MysqlDataTruncation ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen frissítés");
            alert.setHeaderText("Az egyik frissítendő adat túl hosszú.");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<Course> getCourses(String sql) {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                courses.add(createCourse(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courses;
    }

    private Course createCourse(ResultSet rs) {
        Course course = null;
        try {
            course = new Course(
                rs.getString("kurzus_kod"),
                rs.getShort("kredit_ertek"),
                rs.getBoolean("gyakorlat"),
                rs.getString("nev"),
                rs.getString("etr_kod")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return course;
    }

}
