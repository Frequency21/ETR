package main.dao;

import main.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO extends DAO {

    public List<Subject> getSubjectsForStudent(String etrCode) {
        List<Subject> subjects = new ArrayList<>();
        String sqlFindSubjects = "select kurzus.kurzus_kod, nev, kredit_ertek, erdemjegy, ev, felev from felvett " +
            "natural join tanora inner join kurzus on tanora.kurzus_kod = kurzus.kurzus_kod\n" +
            "where felvett.etr_kod = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sqlFindSubjects)) {
            stmt.setString(1, etrCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                subjects.add(getSubject(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return subjects;
    }

    private Subject getSubject(ResultSet rs) {
        Subject subject = null;
        try {
            subject = new Subject(
                rs.getString("kurzus.kurzus_kod"),
                rs.getString("nev"),
                rs.getShort("kredit_ertek"),
                rs.getByte("erdemjegy"),
                rs.getShort("ev"),
                rs.getByte("felev")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return subject;
    }
}
