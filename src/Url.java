package src;

import java.sql.Timestamp;

public class Url {

    private int id;
    private String originalUrl;
    private String shortCode;
    private int clicks;
    private Timestamp expiryTime;
    private String createdAt;

    // Default constructor
    public Url() {}

    // Full constructor (with expiry)
    public Url(int id, String originalUrl, String shortCode, int clicks, Timestamp expiryTime) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.clicks = clicks;
        this.expiryTime = expiryTime;
    }

    // Constructor without expiry
    public Url(int id, String originalUrl, String shortCode, int clicks) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.clicks = clicks;
        this.expiryTime = null;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public int getClicks() { return clicks; }
    public void setClicks(int clicks) { this.clicks = clicks; }

    public Timestamp getExpiryTime() { return expiryTime; }
    public void setExpiryTime(Timestamp expiryTime) { this.expiryTime = expiryTime; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    public boolean isExpired() {
        return expiryTime != null && expiryTime.before(new java.util.Date());
    }

    @Override
    public String toString() {
        String expiry = (expiryTime != null) ? expiryTime.toString() : "No Expiry";
        return "+------------------------------------------------------------+\n"
             + "  ID          : " + id + "\n"
             + "  Original URL: " + originalUrl + "\n"
             + "  Short Code  : " + shortCode + "\n"
             + "  Short URL   : http://localhost/" + shortCode + "\n"
             + "  Clicks      : " + clicks + "\n"
             + "  Expires On  : " + expiry + "\n"
             + "+------------------------------------------------------------+";
    }
}
