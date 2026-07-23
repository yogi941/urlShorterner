package src;

import java.sql.Timestamp;

/**
 * Model representing a single click/redirect event on a short URL.
 * Stored in the url_clicks table.
 */
public class ClickAnalytics {

    private int id;
    private int urlId;
    private String shortCode;
    private String country;
    private String browser;
    private String device;
    private Timestamp clickedAt;

    // Default constructor
    public ClickAnalytics() {}

    // Full constructor
    public ClickAnalytics(int id, int urlId, String shortCode,
                          String country, String browser,
                          String device, Timestamp clickedAt) {
        this.id        = id;
        this.urlId     = urlId;
        this.shortCode = shortCode;
        this.country   = country;
        this.browser   = browser;
        this.device    = device;
        this.clickedAt = clickedAt;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public int getUrlId()           { return urlId; }
    public void setUrlId(int urlId) { this.urlId = urlId; }

    public String getShortCode()                    { return shortCode; }
    public void   setShortCode(String shortCode)    { this.shortCode = shortCode; }

    public String getCountry()                  { return country; }
    public void   setCountry(String country)    { this.country = country; }

    public String getBrowser()                  { return browser; }
    public void   setBrowser(String browser)    { this.browser = browser; }

    public String getDevice()                   { return device; }
    public void   setDevice(String device)      { this.device = device; }

    public Timestamp getClickedAt()                     { return clickedAt; }
    public void      setClickedAt(Timestamp clickedAt)  { this.clickedAt = clickedAt; }

    @Override
    public String toString() {
        return String.format("  [%s] Code=%-8s Country=%-15s Browser=%-15s Device=%s",
                clickedAt, shortCode, country, browser, device);
    }
}
