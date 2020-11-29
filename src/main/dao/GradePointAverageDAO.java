package main.dao;

import main.model.GradePointAverage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradePointAverageDAO extends DAO {
    public List<GradePointAverage> getGPAs() {
        List<GradePointAverage> gpa = new ArrayList<>();
        String sql = "select felh.etr_kod, felh.vnev, felh.knev, ev, felev, avg(erdemjegy) as atlag, " +
            "sum(kredit_ertek * erdemjegy) / 30 as KKI, sum(kredit_ertek) as `ossz kredit` from felvett as felv " +
            "natural join tanora t inner join kurzus k on t.kurzus_kod = k.kurzus_kod left join felhasznalo felh " +
            "on felv.etr_kod = felh.etr_kod where erdemjegy != 1 group by felh.etr_kod, ev, felev";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                gpa.add(createGradePointAverage(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return gpa;
    }

    private GradePointAverage createGradePointAverage(ResultSet rs) {
        GradePointAverage gpa = null;
        try {
            gpa = new GradePointAverage(
                rs.getString("felh.etr_kod"),
                rs.getString("felh.vnev"),
                rs.getString("felh.knev"),
                rs.getShort("ev"),
                rs.getByte("felev"),
                rs.getDouble("atlag"),
                rs.getDouble("KKI"),
                rs.getInt("ossz kredit")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return gpa;
    }
}
