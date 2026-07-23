package src;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Singleton-style database connection utility.
 * Provides a MySQL connection using JDBC.
 *
 * ⚠️  Before running, make sure:
 *   1. MySQL server is running.
 *   2. You have created the database: CREATE DATABASE url_shortener;
 *   3. You have created the table (see schema below).
 *   4. Update DB_USER and DB_PASSWORD with your credentials.
 *
 * SQL Schema:
 * ─────────────────────────────────────────────
 * CREATE TABLE urls (
 *     id           INT AUTO_INCREMENT PRIMARY KEY,
 *     original_url VARCHAR(2048) NOT NULL,
 *     short_code   VARCHAR(20) UNIQUE,
 *     clicks       INT DEFAULT 0,
 *     expiry_time  TIMESTAMP NULL,
 *     created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 * );
 * ─────────────────────────────────────────────
 */
public class DBConnection {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/url_shortener";
    private static final String DB_USER     = "root";       // ← change if needed
    private static final String DB_PASSWORD = "";           // ← change if needed
    private static final String DRIVER      = "com.mysql.cj.jdbc.Driver";

    /**
     * Returns a new MySQL Connection object.
     * Call this inside a try-with-resources block to auto-close.
     */
    public static Connection getConnection() throws Exception {
        Class.forName(DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
