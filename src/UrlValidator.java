package src;

import java.net.URI;
import java.net.URL;

/**
 * Utility class for validating URLs before saving them.
 */
public class UrlValidator {

    /**
     * Returns true if the given string is a valid HTTP/HTTPS URL.
     *
     * @param urlStr the URL string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String urlStr) {
        if (urlStr == null || urlStr.isBlank()) {
            return false;
        }

        if (!urlStr.startsWith("http://") && !urlStr.startsWith("https://")) {
            return false;
        }

        try {
            URI uri = new URI(urlStr);
            URL url = uri.toURL();
            // Must have a valid host
            return url.getHost() != null && !url.getHost().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
