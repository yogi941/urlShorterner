package src;

import java.util.Locale;

/**
 * Utility that auto-detects device info from the system environment.
 *
 * In a console/desktop app:
 *   - Device  → OS name (Windows 10, Linux, Mac OS X)
 *   - Browser → "Console/CLI" (no browser in console mode)
 *   - Country → System locale display country (e.g., India, United States)
 *
 * In a real REST/Web app you would parse:
 *   - User-Agent header  → Browser + Device
 *   - IP address         → Country (via GeoIP lookup)
 */
public class DeviceInfo {

    /**
     * Returns the OS name as the "device" (e.g., Windows 10, Linux, Mac OS X).
     */
    public static String getDevice() {
        String os = System.getProperty("os.name");
        if (os == null || os.isBlank()) return "Unknown";

        String lower = os.toLowerCase();
        if (lower.contains("win"))   return "Windows";
        if (lower.contains("mac"))   return "Mac OS";
        if (lower.contains("linux")) return "Linux";
        if (lower.contains("android")) return "Android";
        return os;
    }

    /**
     * Returns "Console/CLI" since there is no browser in a console app.
     * In a REST API, you would parse the HTTP User-Agent header instead.
     */
    public static String getBrowser() {
        return "Console/CLI";
    }

    /**
     * Returns the country name from the system's default locale.
     * Example: "India", "United States", "Germany"
     */
    public static String getCountry() {
        String country = Locale.getDefault().getDisplayCountry(Locale.ENGLISH);
        return (country == null || country.isBlank()) ? "Unknown" : country;
    }

    /**
     * Returns a summary string for logging.
     */
    public static String getSummary() {
        return "Device=" + getDevice() +
               " | Browser=" + getBrowser() +
               " | Country=" + getCountry();
    }
}
