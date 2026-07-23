package src;

import java.sql.Timestamp;
import java.util.List;

/**
 * Business Logic Layer — orchestrates repository calls and applies rules.
 *
 * Features:
 *  ① Auto short URL generation (Base62)
 *  ② Custom alias with collision detection
 *  ③ URL expiry support
 *  ④ Click tracking on redirect
 *  ⑤ URL validation
 *  ⑥ Statistics
 *  ⑦ Analytics logging (Country, Browser, Device, Time)
 */
public class UrlService {

    private final UrlRepository       repository      = new UrlRepository();
    private final AnalyticsRepository analyticsRepo   = new AnalyticsRepository();

    // ─── 1. Shorten URL (auto-generated Base62 code) ───────────────────────────

    public void shortenUrl(String originalUrl) {
        if (!UrlValidator.isValid(originalUrl)) {
            System.out.println("  ❌ Invalid URL. Must start with http:// or https://");
            return;
        }

        int id = repository.saveUrl(originalUrl);
        if (id == -1) {
            System.out.println("  ❌ Failed to save URL.");
            return;
        }

        String shortCode = Base62.encode(id);
        repository.updateShortCode(id, shortCode);

        System.out.println("  ✅ Short URL : http://localhost/" + shortCode);
    }

    // ─── 2. Open / Redirect URL (with expiry + analytics logging) ─────────────

    public void openUrl(String shortCode) {
        Url url = repository.findByShortCode(shortCode);

        if (url == null) {
            System.out.println("  ❌ URL not found for code: " + shortCode);
            return;
        }

        // Expiry check
        if (url.getExpiryTime() != null &&
            url.getExpiryTime().before(new java.util.Date())) {
            System.out.println("  ⏰ This URL has expired.");
            return;
        }

        // Increment click counter in urls table
        repository.incrementClicks(url.getId());

        // ── Log analytics (auto-detect device info) ──────────────────────────
        String country = DeviceInfo.getCountry();
        String browser = DeviceInfo.getBrowser();
        String device  = DeviceInfo.getDevice();

        analyticsRepo.logClick(url.getId(), shortCode, country, browser, device);

        // ── Output ────────────────────────────────────────────────────────────
        System.out.println("  🔗 Redirecting to  : " + url.getOriginalUrl());
        System.out.println("  🌍 Country detected : " + country);
        System.out.println("  💻 Device detected  : " + device);
        System.out.println("  🌐 Browser detected : " + browser);
    }

    // ─── 3. View all URLs ───────────────────────────────────────────────────────

    public void viewAllUrls() {
        List<Url> urls = repository.getAllUrls();
        if (urls.isEmpty()) {
            System.out.println("  No URLs found.");
            return;
        }
        System.out.println("\n  Total URLs: " + urls.size());
        for (Url url : urls) {
            System.out.println(url);
        }
    }

    // ─── 4. Delete URL ──────────────────────────────────────────────────────────

    public void deleteUrl(int id) {
        boolean deleted = repository.deleteUrl(id);
        if (deleted) {
            System.out.println("  ✅ URL with ID " + id + " deleted.");
        } else {
            System.out.println("  ❌ No URL found with ID " + id);
        }
    }

    // ─── 5. Statistics (single URL) ────────────────────────────────────────────

    public void showStatistics(String shortCode) {
        Url url = repository.findByShortCode(shortCode);
        if (url == null) {
            System.out.println("  ❌ URL not found.");
            return;
        }

        String expiry = (url.getExpiryTime() != null)
                ? url.getExpiryTime().toString() : "Never";

        System.out.println("\n  ────── URL Statistics ──────");
        System.out.println("  Original URL : " + url.getOriginalUrl());
        System.out.println("  Short Code   : " + url.getShortCode());
        System.out.println("  Total Clicks : " + url.getClicks());
        System.out.println("  Expires On   : " + expiry);
        System.out.println("  Status       : " + (url.isExpired() ? "❌ Expired" : "✅ Active"));
        System.out.println("  ───────────────────────────");
    }

    // ─── 6. Global statistics ───────────────────────────────────────────────────

    public void showGlobalStatistics() {
        System.out.println("\n  ────── Global Statistics ──────");
        System.out.println("  Total URLs   : " + repository.getTotalUrls());
        System.out.println("  Total Clicks : " + repository.getTotalClicks());
        System.out.println("  ───────────────────────────────");
    }

    // ─── 7. Custom Alias URL ────────────────────────────────────────────────────

    public void shortenCustomUrl(String originalUrl, String customCode) {
        if (!UrlValidator.isValid(originalUrl)) {
            System.out.println("  ❌ Invalid URL. Must start with http:// or https://");
            return;
        }
        if (customCode == null || customCode.isBlank()) {
            System.out.println("  ❌ Custom code cannot be empty.");
            return;
        }
        if (!customCode.matches("[a-zA-Z0-9\\-]+")) {
            System.out.println("  ❌ Custom code must contain only letters, numbers, or hyphens.");
            return;
        }
        if (repository.existsByShortCode(customCode)) {
            System.out.println("  ❌ Short code '" + customCode + "' already taken. Choose another.");
            return;
        }

        boolean saved = repository.saveCustomUrl(originalUrl, customCode);
        if (saved) {
            System.out.println("  ✅ Short URL : http://localhost/" + customCode);
        } else {
            System.out.println("  ❌ Failed to save. Code might be a duplicate.");
        }
    }

    // ─── 8. Shorten URL with Expiry ─────────────────────────────────────────────

    public void shortenWithExpiry(String originalUrl, int days) {
        if (!UrlValidator.isValid(originalUrl)) {
            System.out.println("  ❌ Invalid URL. Must start with http:// or https://");
            return;
        }
        if (days <= 0) {
            System.out.println("  ❌ Days must be greater than 0.");
            return;
        }

        Timestamp expiry = new Timestamp(
            System.currentTimeMillis() + (long) days * 24 * 60 * 60 * 1000
        );

        int id = repository.saveUrlWithExpiry(originalUrl, expiry);
        if (id == -1) {
            System.out.println("  ❌ Failed to save URL.");
            return;
        }

        String shortCode = Base62.encode(id);
        repository.updateShortCode(id, shortCode);

        System.out.println("  ✅ Short URL   : http://localhost/" + shortCode);
        System.out.println("  ⏳ Expires on  : " + expiry);
    }
}
