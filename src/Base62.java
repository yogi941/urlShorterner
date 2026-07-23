package src;

/**
 * Base62 encoder/decoder utility.
 * Encodes numeric IDs into short alphanumeric strings using characters [0-9A-Za-z].
 * This is used to generate short URL codes from numeric IDs.
 */
public class Base62 {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    /**
     * Encodes a long number into a Base62 string.
     *
     * @param number the number to encode (must be >= 0)
     * @return the Base62 encoded string
     */
    public static String encode(long number) {
        if (number == 0) {
            return String.valueOf(CHARACTERS.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % BASE);
            sb.append(CHARACTERS.charAt(remainder));
            number /= BASE;
        }
        return sb.reverse().toString();
    }

    /**
     * Decodes a Base62 string back to a long number.
     *
     * @param encoded the Base62 string to decode
     * @return the decoded long number
     */
    public static long decode(String encoded) {
        long result = 0;
        for (char c : encoded.toCharArray()) {
            result = result * BASE + CHARACTERS.indexOf(c);
        }
        return result;
    }

    /**
     * Generates a short code from a given ID, padded to a fixed length.
     *
     * @param id     the numeric ID
     * @param length the desired minimum length of the code (padded with '0')
     * @return the short code string
     */
    public static String generateCode(long id, int length) {
        String encoded = encode(id);
        while (encoded.length() < length) {
            encoded = "0" + encoded;
        }
        return encoded;
    }
}
