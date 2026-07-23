package src;

/**
 * Optional: PrimeIdGenerator generates pseudo-random unique IDs using a
 * multiplicative inverse trick over a prime number space.
 * This avoids sequential/predictable short codes.
 */
public class PrimeIdGenerator {

    // A large prime number slightly above 10^9
    private static final long PRIME = 1_000_000_007L;

    // A random coprime multiplier (must be coprime with PRIME)
    private static final long MULTIPLIER = 387_420_489L;

    // Offset to avoid zero-based outputs
    private static final long OFFSET = 123_456_789L;

    /**
     * Obfuscates a sequential ID into a pseudo-random unique ID.
     * Two different sequential IDs will always produce different output IDs.
     *
     * @param sequentialId the sequential ID (from auto-increment DB)
     * @return a pseudo-random unique ID
     */
    public static long obfuscate(long sequentialId) {
        return (sequentialId * MULTIPLIER + OFFSET) % PRIME;
    }

    /**
     * Reverses the obfuscation to recover the original sequential ID.
     * Uses the modular inverse of MULTIPLIER.
     *
     * @param obfuscatedId the obfuscated ID
     * @return the original sequential ID
     */
    public static long deobfuscate(long obfuscatedId) {
        long modularInverse = modInverse(MULTIPLIER, PRIME);
        return ((obfuscatedId - OFFSET + PRIME) % PRIME * modularInverse) % PRIME;
    }

    /**
     * Computes the modular inverse using Fermat's Little Theorem.
     * Only valid when PRIME is a prime number.
     */
    private static long modInverse(long a, long p) {
        return modPow(a, p - 2, p);
    }

    /**
     * Fast modular exponentiation: base^exp % mod
     */
    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = result * base % mod;
            }
            exp >>= 1;
            base = base * base % mod;
        }
        return result;
    }

    /**
     * Generates a short code from a sequential ID with obfuscation.
     *
     * @param sequentialId the sequential DB ID
     * @return a short Base62 code
     */
    public static String generateShortCode(long sequentialId) {
        long obfuscatedId = obfuscate(sequentialId);
        return Base62.encode(obfuscatedId);
    }
}
