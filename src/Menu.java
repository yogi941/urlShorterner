package src;

import java.util.Scanner;

/**
 * Console Menu — user-facing interface for the URL Shortener.
 *
 * Options:
 *   1. Shorten URL              (auto Base62 code)
 *   2. Open URL                 (redirect + click tracking + analytics log)
 *   3. View All URLs
 *   4. Delete URL
 *   5. URL Statistics           (single URL)
 *   6. Global Statistics
 *   7. Custom Alias URL         (user-defined short code)
 *   8. Shorten URL with Expiry
 *   9. 📊 View Analytics        (Country / Browser / Device / Time)
 *  10. Exit
 */
public class Menu {

    private final UrlService       service          = new UrlService();
    private final AnalyticsService analyticsService = new AnalyticsService();
    private final Scanner          sc               = new Scanner(System.in);

    public void start() {
        System.out.println("\n  ╔══════════════════════════════╗");
        System.out.println("  ║   🔗 URL SHORTENER  v2.0     ║");
        System.out.println("  ║   With Analytics Support     ║");
        System.out.println("  ╚══════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("\n  Choice: ");
            String input = sc.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️  Please enter a valid number.");
                continue;
            }

            System.out.println();

            switch (choice) {

                // ── 1. Shorten URL ──────────────────────────────────────────────
                case 1 -> {
                    System.out.print("  Enter URL: ");
                    service.shortenUrl(sc.nextLine().trim());
                }

                // ── 2. Open / Redirect ──────────────────────────────────────────
                case 2 -> {
                    System.out.print("  Enter short code: ");
                    service.openUrl(sc.nextLine().trim());
                }

                // ── 3. View All URLs ────────────────────────────────────────────
                case 3 -> service.viewAllUrls();

                // ── 4. Delete URL ───────────────────────────────────────────────
                case 4 -> {
                    System.out.print("  Enter URL ID to delete: ");
                    try {
                        service.deleteUrl(Integer.parseInt(sc.nextLine().trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("  ⚠️  Invalid ID.");
                    }
                }

                // ── 5. URL Statistics ───────────────────────────────────────────
                case 5 -> {
                    System.out.print("  Enter short code: ");
                    service.showStatistics(sc.nextLine().trim());
                }

                // ── 6. Global Statistics ────────────────────────────────────────
                case 6 -> service.showGlobalStatistics();

                // ── 7. Custom Alias URL ─────────────────────────────────────────
                case 7 -> {
                    System.out.print("  Enter URL: ");
                    String original = sc.nextLine().trim();
                    System.out.print("  Enter custom code: ");
                    String custom = sc.nextLine().trim();
                    service.shortenCustomUrl(original, custom);
                }

                // ── 8. Shorten with Expiry ──────────────────────────────────────
                case 8 -> {
                    System.out.print("  Enter URL: ");
                    String url = sc.nextLine().trim();
                    System.out.print("  Expire after how many days? ");
                    try {
                        int days = Integer.parseInt(sc.nextLine().trim());
                        service.shortenWithExpiry(url, days);
                    } catch (NumberFormatException e) {
                        System.out.println("  ⚠️  Invalid number of days.");
                    }
                }

                // ── 9. View Full Analytics ──────────────────────────────────────
                case 9 -> {
                    System.out.print("  Enter short code to analyse: ");
                    String code = sc.nextLine().trim();
                    analyticsService.showFullReport(code);
                }

                // ── 10. Exit ────────────────────────────────────────────────────
                case 10 -> {
                    System.out.println("  👋 Goodbye!");
                    running = false;
                }

                default -> System.out.println("  ⚠️  Invalid option. Choose 1–10.");
            }

            System.out.println();
        }

        sc.close();
    }

    private void printMenu() {
        System.out.println("  ┌──────────────────────────────────┐");
        System.out.println("  │  1.  Shorten URL                 │");
        System.out.println("  │  2.  Open / Redirect URL         │");
        System.out.println("  │  3.  View All URLs               │");
        System.out.println("  │  4.  Delete URL                  │");
        System.out.println("  │  5.  URL Statistics              │");
        System.out.println("  │  6.  Global Statistics           │");
        System.out.println("  │  7.  Custom Alias URL            │");
        System.out.println("  │  8.  Shorten URL with Expiry     │");
        System.out.println("  │  9.  📊 View Analytics           │");
        System.out.println("  │  10. Exit                        │");
        System.out.println("  └──────────────────────────────────┘");
    }
}
