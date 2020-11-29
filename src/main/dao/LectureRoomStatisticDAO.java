package main.dao;

import main.model.GradePointAverage;
import main.model.LectureRoomStatistic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureRoomStatisticDAO extends DAO {

    public List<LectureRoomStatistic> getStatistics() {
        List<LectureRoomStatistic> statistics = new ArrayList<>();
        String sql = "select te.epulet_kod, te.terem_kod, kabinet, te.max_letszam, ev, felev, COUNT(kurzus_kod) as mennyi " +
            "from terem as te inner join tanora ta on te.epulet_kod = ta.epulet_kod and te.terem_kod = ta.terem_kod " +
            "group by te.epulet_kod, te.terem_kod, ev, felev order by ev, felev, te.epulet_kod, te.terem_kod";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                statistics.add(createStatistic(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statistics;
    }

    private LectureRoomStatistic createStatistic(ResultSet rs) {
        LectureRoomStatistic statistic = null;
        try {
            statistic = new LectureRoomStatistic(
                rs.getString("te.epulet_kod"),
                rs.getString("te.terem_kod"),
                rs.getBoolean("kabinet"),
                rs.getShort("te.max_letszam"),
                rs.getShort("ev"),
                rs.getByte("felev"),
                rs.getInt("mennyi")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statistic;
    }
}
