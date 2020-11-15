package main.dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.scene.control.Alert;
import main.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO extends DAO {

    public List<Teacher> getTeachers() {
        String sql = "SELECT f.etr_kod, f.vnev, f.knev, tanszek, f.email FROM " +
            "oktato INNER JOIN felhasznalo f " +
            "ON oktato.etr_kod = f.etr_kod";
        return getTeachers(sql);
    }

    public List<Teacher> getTeachersForName(String firstName, String lastName) {
        String sql = "SELECT * FROM oktato WHERE vnev like ? AND knev like ?";
        List<Teacher> teachers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                teachers.add(createTeacher(rs));
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teachers;
    }

    public void addTeacher(String firstName, String lastName, String department, String email) throws SQLIntegrityConstraintViolationException {
        String sqlUser = "INSERT INTO felhasznalo VALUES (?, ?, ?, ?)",
            sqlTeacher = "INSERT INTO oktato VALUES (?, ?)";
        String prefix = ETRCodeMaker.etrCodePrefix(firstName, lastName);
//        Hány olyan felhasználó van, akinek azonos etr kódra képződne le a neve?
        String sqlCount = "SELECT count(*) AS darab FROM felhasznalo WHERE etr_kod like ?";
        int count = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtCount = connection.prepareStatement(sqlCount);
             PreparedStatement stmtUser = connection.prepareStatement(sqlUser);
             PreparedStatement stmtTeacher = connection.prepareStatement(sqlTeacher)) {
            stmtCount.setString(1, prefix + "%");
            ResultSet rs = stmtCount.executeQuery();
            if (rs.next())
                count = rs.getInt("darab");
            String etrCode = ETRCodeMaker.createEtrCode(firstName, lastName, count);
            stmtUser.setString(1, etrCode);
            stmtUser.setString(2, firstName);
            stmtUser.setString(3, lastName);
            stmtUser.setString(4, email);
            int rowsUser = stmtUser.executeUpdate();
            stmtTeacher.setString(1, etrCode);
            stmtTeacher.setString(2, department);
            int rowsTeacher = stmtTeacher.executeUpdate();
            Alert alert;
            if (rowsUser == 1 && rowsTeacher == 1) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Az oktató hozzáadása sikeres volt.");
            } else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Az oktató hozzáadása sikertelen volt.");
            }
            alert.setTitle("Új oktató felvitele");
            alert.show();
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw ex;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteTeacher(String etrCode) {
        String sql = "DELETE FROM felhasznalo WHERE etr_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, etrCode);
            int rowDeleted = stmt.executeUpdate();
            Alert alert;
            if (rowDeleted == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A törlés sikertelen volt, nincs ilyen oktató.");
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

    public void updateTeacher(String etrCode, String firstName, String lastName, String department, String email) {
        String sqlUpdFelh = "UPDATE felhasznalo SET vnev = ?, knev = ?, email = ? WHERE etr_kod = ?";
        String sqlUpdTeacher = "UPDATE oktato SET tanszek = ? WHERE etr_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtFelh = connection.prepareStatement(sqlUpdFelh);
             PreparedStatement stmtTeacher = connection.prepareStatement(sqlUpdTeacher)) {
            stmtFelh.setString(1, firstName);
            stmtFelh.setString(2, lastName);
            stmtFelh.setString(3, email);
            stmtFelh.setString(4, etrCode);
            stmtTeacher.setString(1, department);
            stmtTeacher.setString(2, etrCode);
            int rowUpdFelh = stmtFelh.executeUpdate();
            int rowUpdTeacher = stmtTeacher.executeUpdate();
            Alert alert;
            if (rowUpdFelh == 0 || rowUpdTeacher == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A frissítés sikertelen volt, nincs ilyen oktató.");
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

    private List<Teacher> getTeachers(String sql) {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                teachers.add(createTeacher(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teachers;
    }

    private Teacher createTeacher(ResultSet rs) {
        Teacher teacher = null;
        try {
            teacher = new Teacher(
                rs.getString("etr_kod"),
                rs.getString("vnev"),
                rs.getString("knev"),
                rs.getString("tanszek"),
                rs.getString("email")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teacher;
    }

}
