package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Layer — all SQL operations for the urls table.
 */
public class UrlRepository {

    // ─── Save basic URL (returns generated ID) ─────────────────────────────────

    public int saveUrl(String originalUrl) {
        String sql = "INSERT INTO urls(original_url) VALUES(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, originalUrl);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ─── Update short code after Base62 encoding ────────────────────────────────

    public void updateShortCode(int id, String shortCode) {
        String sql = "UPDATE urls SET short_code=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── Check if a short code already exists (collision check) ────────────────

    public boolean existsByShortCode(String shortCode) {
        String sql = "SELECT id FROM urls WHERE short_code=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ─── Save URL with a custom alias ──────────────────────────────────────────

    public boolean saveCustomUrl(String originalUrl, String shortCode) {
        String sql = "INSERT INTO urls(original_url, short_code) VALUES(?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, originalUrl);
            ps.setString(2, shortCode);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            // SQLIntegrityConstraintViolationException → duplicate alias
            return false;
        }
    }

    // ─── Save URL with expiry (returns generated ID) ───────────────────────────

    public int saveUrlWithExpiry(String originalUrl, Timestamp expiry) {
        String sql = "INSERT INTO urls(original_url, expiry_time) VALUES(?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, originalUrl);
            ps.setTimestamp(2, expiry);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ─── Find URL by short code ─────────────────────────────────────────────────

    public Url findByShortCode(String shortCode) {
        String sql = "SELECT * FROM urls WHERE short_code=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shortCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Url(
                    rs.getInt("id"),
                    rs.getString("original_url"),
                    rs.getString("short_code"),
                    rs.getInt("clicks"),
                    rs.getTimestamp("expiry_time")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ─── Increment click count ──────────────────────────────────────────────────

    public void incrementClicks(int id) {
        String sql = "UPDATE urls SET clicks = clicks + 1 WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── Get all URLs ───────────────────────────────────────────────────────────

    public List<Url> getAllUrls() {
        List<Url> list = new ArrayList<>();
        String sql = "SELECT * FROM urls ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Url(
                    rs.getInt("id"),
                    rs.getString("original_url"),
                    rs.getString("short_code"),
                    rs.getInt("clicks"),
                    rs.getTimestamp("expiry_time")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ─── Delete URL by ID ───────────────────────────────────────────────────────

    public boolean deleteUrl(int id) {
        String sql = "DELETE FROM urls WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ─── Get total URLs count ───────────────────────────────────────────────────

    public int getTotalUrls() {
        String sql = "SELECT COUNT(*) FROM urls";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ─── Get total clicks across all URLs ──────────────────────────────────────

    public int getTotalClicks() {
        String sql = "SELECT SUM(clicks) FROM urls";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
