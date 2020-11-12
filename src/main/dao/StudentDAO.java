package main.dao;

import main.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO extends DAO {

    private static final Map<Character, Character> makeAscii = new HashMap<>();
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static {
        makeAscii.put('Ö', 'O');
        makeAscii.put('Ő', 'O');
        makeAscii.put('Ó', 'O');
        makeAscii.put('Ü', 'U');
        makeAscii.put('Ű', 'U');
        makeAscii.put('Ú', 'U');
        makeAscii.put('É', 'E');
        makeAscii.put('Á', 'A');
    }

    public List<Student> getStudents() {
        String sql = "SELECT f.etr_kod, f.vnev, f.knev, szak, evfolyam, f.email FROM " +
                "hallgato INNER JOIN felhasznalo f " +
                "ON hallgato.etr_kod = f.etr_kod";
        return getStudents(sql);
    }

    public void addStudent(Student student) {
//        String sql = ;
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

    private static String createEtrCode(String firstName, String lastName, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeAscii.containsKey(firstName.toUpperCase().charAt(0)) ?
                makeAscii.get(firstName.toUpperCase().charAt(0)) :
                firstName.toUpperCase().charAt(0));
        sb.append(makeAscii.containsKey(firstName.toUpperCase().charAt(1)) ?
                makeAscii.get(firstName.toUpperCase().charAt(1)) :
                firstName.toUpperCase().charAt(1));
        sb.append(makeAscii.containsKey(lastName.toUpperCase().charAt(0)) ?
                makeAscii.get(lastName.toUpperCase().charAt(0)) :
                lastName.toUpperCase().charAt(0));
        int q = count / 26;
        int q2 = q / 26;
        int q3 = q2 / 26;
        sb.append(alphabet.charAt((q2 + 24) % 26));
        sb.append(alphabet.charAt(q % 26));
        sb.append(alphabet.charAt(count % 26));
        sb.append(alphabet.charAt((q3 + 19) % 26));
        sb.append(".SZE");
        return sb.toString();
    }

}
