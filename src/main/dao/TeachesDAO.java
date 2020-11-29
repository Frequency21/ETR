package main.dao;

import main.model.GradePointAverage;
import main.model.Teaches;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TeachesDAO extends DAO {

    public List<Teaches> getTeaches() {
        List<Teaches> teaches = new ArrayList<>();
        String sql = "select f.etr_kod, vnev, knev, k.kurzus_kod, nev, ev, felev, " +
            "nap, tan.kezdes, vegzes, t.epulet_kod, t.terem_kod " +
            "from oktato o natural join felhasznalo as f natural join tart t natural join tanora tan " +
            "inner join kurzus k on tan.kurzus_kod = k.kurzus_kod;";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                teaches.add(createTeaches(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teaches;
    }

    private Teaches createTeaches(ResultSet rs) {
        Teaches teaches = null;
        try {
            teaches = new Teaches(
                rs.getString("f.etr_kod"),
                rs.getString("vnev"),
                rs.getString("knev"),
                rs.getString("k.kurzus_kod"),
                rs.getString("nev"),
                rs.getShort("ev"),
                rs.getByte("felev"),
                rs.getString("nap"),
                rs.getTime("tan.kezdes").toLocalTime(),
                rs.getTime("vegzes").toLocalTime(),
                rs.getString("t.epulet_kod"),
                rs.getString("t.terem_kod")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teaches;
    }
}
