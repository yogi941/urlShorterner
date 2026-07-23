package src;

import java.util.List;
import java.util.Map;

/**
 * Analytics Service — formats and displays click analytics in the console.
 *
 * Shows:
 *   ① Total clicks
 *   ② Clicks by Country    (bar chart)
 *   ③ Clicks by Browser    (bar chart)
 *   ④ Clicks by Device     (bar chart)
 *   ⑤ Clicks by Day        (timeline)
 *   ⑥ Recent 5 clicks      (live feed)
 */
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepo = new AnalyticsRepository();

    // Maximum bar width for charts
    private static final int BAR_MAX_WIDTH = 30;

    // ─── Full analytics report ─────────────────────────────────────────────────

    public void showFullReport(String shortCode) {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║      📊 ANALYTICS REPORT                 ║");
        System.out.println("  ║      Short Code: " + padRight(shortCode, 24) + "║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        int total = analyticsRepo.getTotalClicks(shortCode);
        System.out.println("\n  🖱  Total Clicks : " + total);

        if (total == 0) {
            System.out.println("\n  No click data yet. Open the URL first!");
            return;
        }

        // Country breakdown
        printBarChart("🌍 Clicks by Country",
                analyticsRepo.getClicksByCountry(shortCode), total);

        // Browser breakdown
        printBarChart("🌐 Clicks by Browser",
                analyticsRepo.getClicksByBrowser(shortCode), total);

        // Device breakdown
        printBarChart("💻 Clicks by Device",
                analyticsRepo.getClicksByDevice(shortCode), total);

        // Timeline by day
        printTimeline("📅 Clicks by Day",
                analyticsRepo.getClicksByDay(shortCode));

        // Recent 5 clicks
        printRecentClicks(shortCode, 5);
    }

    // ─── Bar chart printer ─────────────────────────────────────────────────────

    private void printBarChart(String title, Map<String, Integer> data, int total) {
        if (data.isEmpty()) return;

        System.out.println("\n  ── " + title + " ──────────────────────");

        int maxVal = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label   = padRight(entry.getKey(), 16);
            int    count   = entry.getValue();
            int    barLen  = (int) ((double) count / maxVal * BAR_MAX_WIDTH);
            double pct     = (double) count / total * 100;

            String bar = "█".repeat(barLen);
            System.out.printf("  %-16s │ %-30s %3d  (%.1f%%)%n",
                    label, bar, count, pct);
        }
    }

    // ─── Timeline printer ──────────────────────────────────────────────────────

    private void printTimeline(String title, Map<String, Integer> data) {
        if (data.isEmpty()) return;

        System.out.println("\n  ── " + title + " ─────────────────────────");

        int maxVal = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int barLen = (int) ((double) entry.getValue() / maxVal * BAR_MAX_WIDTH);
            String bar = "▪".repeat(barLen);
            System.out.printf("  %s │ %-30s %d clicks%n",
                    entry.getKey(), bar, entry.getValue());
        }
    }

    // ─── Recent clicks feed ────────────────────────────────────────────────────

    private void printRecentClicks(String shortCode, int limit) {
        List<ClickAnalytics> recent = analyticsRepo.getRecentClicks(shortCode, limit);
        if (recent.isEmpty()) return;

        System.out.println("\n  ── 🕐 Recent Clicks ─────────────────────────");
        for (ClickAnalytics c : recent) {
            System.out.println(c);
        }
    }

    // ─── Quick summary (shown after redirect) ─────────────────────────────────

    public void showQuickSummary(String shortCode) {
        int total = analyticsRepo.getTotalClicks(shortCode);
        System.out.println("  📊 Total clicks on /" + shortCode + " : " + total);
    }

    // ─── Helper ────────────────────────────────────────────────────────────────

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
