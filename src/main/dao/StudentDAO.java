package main.dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.scene.control.Alert;
import main.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends DAO {

    public List<Student> getStudents() {
        String sql = "SELECT f.etr_kod, f.vnev, f.knev, szak, evfolyam, f.email FROM " +
            "hallgato INNER JOIN felhasznalo f " +
            "ON hallgato.etr_kod = f.etr_kod";
        return getStudents(sql);
    }

    public List<Student> getStudentsForName(String firstName, String lastName) {
        String sql = "SELECT * FROM hallgato WHERE vnev like ? AND knev like ?";
        List<Student> students = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                students.add(createStudent(rs));
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return students;
    }

    public void addStudent(String firstName, String lastName, String major, short year, String email) {
        String sqlUser = "INSERT INTO felhasznalo VALUES (?, ?, ?, ?)",
            sqlStud = "INSERT INTO hallgato VALUES (?, ?, ?)";
        String prefix = ETRCodeMaker.etrCodePrefix(firstName, lastName);
//        Hány olyan felhasználó van, akinek azonos etr kódra képződne le a neve?
        String sqlCount = "SELECT count(*) AS darab FROM felhasznalo WHERE etr_kod like ?";
        int count = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtCount = connection.prepareStatement(sqlCount);
             PreparedStatement stmtUser = connection.prepareStatement(sqlUser);
             PreparedStatement stmtStud = connection.prepareStatement(sqlStud)) {
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
            stmtStud.setString(1, etrCode);
            stmtStud.setString(2, major);
            stmtStud.setShort(3, year);
            int rowsStud = stmtStud.executeUpdate();
            Alert alert;
            if (rowsUser == 1 && rowsStud == 1) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A hallgató hozzáadása sikeres volt.");
            } else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A hallgató hozzáadása sikertelen volt.");
            }
            alert.setTitle("Új adat felvitele");
            alert.show();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hallgató felvétele");
            alert.setHeaderText("Az email cím már regisztrálva van a rendszerben");
            alert.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteStudent(String etrCode) {
        String sql = "DELETE FROM felhasznalo WHERE etr_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, etrCode);
            int rowDeleted = stmt.executeUpdate();
            Alert alert;
            if (rowDeleted == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A törlés sikertelen volt, nincs ilyen hallgató.");
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

    public void updateStudent(String etrCode, String firstName, String lastName, String major, short year, String email) {
        String sqlUpdFelh = "UPDATE felhasznalo SET vnev = ?, knev = ?, email = ? WHERE etr_kod = ?";
        String sqlUpdHall = "UPDATE hallgato SET szak = ?, evfolyam = ? WHERE etr_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtFelh = connection.prepareStatement(sqlUpdFelh);
             PreparedStatement stmtHall = connection.prepareStatement(sqlUpdHall)) {
            stmtFelh.setString(1, firstName);
            stmtFelh.setString(2, lastName);
            stmtFelh.setString(3, email);
            stmtFelh.setString(4, etrCode);
            stmtHall.setString(1, major);
            stmtHall.setShort(2, year);
            stmtHall.setString(3, etrCode);
            int rowUpdFelh = stmtFelh.executeUpdate();
            int rowUpdHall = stmtHall.executeUpdate();
            Alert alert;
            if (rowUpdFelh == 0 || rowUpdHall == 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("A frissítés sikertelen volt, nincs ilyen hallgató.");
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

    private List<Student> getStudents(String sql) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                students.add(createStudent(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return students;
    }

    private Student createStudent(ResultSet rs) {
        Student student = null;
        try {
            student = new Student(
                rs.getString("etr_kod"),
                rs.getString("vnev"),
                rs.getString("knev"),
                rs.getString("szak"),
                rs.getShort("evfolyam"),
                rs.getString("email")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return student;
    }

}
