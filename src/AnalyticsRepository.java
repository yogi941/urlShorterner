package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Layer for click analytics.
 *
 * SQL table required (run once in MySQL):
 * ─────────────────────────────────────────────────────────────────
 * CREATE TABLE url_clicks (
 *     id         INT AUTO_INCREMENT PRIMARY KEY,
 *     url_id     INT NOT NULL,
 *     short_code VARCHAR(20),
 *     country    VARCHAR(100) DEFAULT 'Unknown',
 *     browser    VARCHAR(100) DEFAULT 'Unknown',
 *     device     VARCHAR(100) DEFAULT 'Unknown',
 *     clicked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 *     FOREIGN KEY (url_id) REFERENCES urls(id) ON DELETE CASCADE
 * );
 * ─────────────────────────────────────────────────────────────────
 */
public class AnalyticsRepository {

    // ─── Log a click event ─────────────────────────────────────────────────────

    public void logClick(int urlId, String shortCode,
                         String country, String browser, String device) {
        String sql = "INSERT INTO url_clicks(url_id, short_code, country, browser, device) " +
                     "VALUES(?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, urlId);
            ps.setString(2, shortCode);
            ps.setString(3, country);
            ps.setString(4, browser);
            ps.setString(5, device);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── Get all clicks for a short code ──────────────────────────────────────

    public List<ClickAnalytics> getClicksByCode(String shortCode) {
        List<ClickAnalytics> list = new ArrayList<>();
        String sql = "SELECT * FROM url_clicks WHERE short_code=? ORDER BY clicked_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ClickAnalytics(
                    rs.getInt("id"),
                    rs.getInt("url_id"),
                    rs.getString("short_code"),
                    rs.getString("country"),
                    rs.getString("browser"),
                    rs.getString("device"),
                    rs.getTimestamp("clicked_at")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ─── Count clicks by country ───────────────────────────────────────────────

    public Map<String, Integer> getClicksByCountry(String shortCode) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT country, COUNT(*) AS total " +
                     "FROM url_clicks WHERE short_code=? " +
                     "GROUP BY country ORDER BY total DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("country"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // ─── Count clicks by browser ───────────────────────────────────────────────

    public Map<String, Integer> getClicksByBrowser(String shortCode) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT browser, COUNT(*) AS total " +
                     "FROM url_clicks WHERE short_code=? " +
                     "GROUP BY browser ORDER BY total DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("browser"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // ─── Count clicks by device ────────────────────────────────────────────────

    public Map<String, Integer> getClicksByDevice(String shortCode) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT device, COUNT(*) AS total " +
                     "FROM url_clicks WHERE short_code=? " +
                     "GROUP BY device ORDER BY total DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("device"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // ─── Count clicks by hour (time analysis) ─────────────────────────────────

    public Map<String, Integer> getClicksByHour(String shortCode) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(clicked_at, '%Y-%m-%d %H:00') AS hour_slot, " +
                     "COUNT(*) AS total " +
                     "FROM url_clicks WHERE short_code=? " +
                     "GROUP BY hour_slot ORDER BY hour_slot ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("hour_slot"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // ─── Count clicks by day ───────────────────────────────────────────────────

    public Map<String, Integer> getClicksByDay(String shortCode) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT DATE(clicked_at) AS day, COUNT(*) AS total " +
                     "FROM url_clicks WHERE short_code=? " +
                     "GROUP BY day ORDER BY day ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("day"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // ─── Total click count for a short code ───────────────────────────────────

    public int getTotalClicks(String shortCode) {
        String sql = "SELECT COUNT(*) FROM url_clicks WHERE short_code=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ─── Recent clicks (last N) ────────────────────────────────────────────────

    public List<ClickAnalytics> getRecentClicks(String shortCode, int limit) {
        List<ClickAnalytics> list = new ArrayList<>();
        String sql = "SELECT * FROM url_clicks WHERE short_code=? " +
                     "ORDER BY clicked_at DESC LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ClickAnalytics(
                    rs.getInt("id"),
                    rs.getInt("url_id"),
                    rs.getString("short_code"),
                    rs.getString("country"),
                    rs.getString("browser"),
                    rs.getString("device"),
                    rs.getTimestamp("clicked_at")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
