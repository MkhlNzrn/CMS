package ru.abolsoft.core.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class UUIDGenerator {
    @NotNull
    @Contract(" -> new")
    public static UUID generateUUIDv7() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] uniqueBytes = generateUniqueBytes();
            byte[] digest = md.digest(uniqueBytes);

            digest[6] &= 0x0f;
            digest[6] |= 0x70;
            digest[8] &= 0x3f;
            digest[8] |= 0x80;

            // Convert the byte array to a long array and create a UUID
            long mostSigBits = 0;
            long leastSigBits = 0;
            for (int i = 0; i < 8; i++) {
                mostSigBits = (mostSigBits << 8) | (digest[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                leastSigBits = (leastSigBits << 8) | (digest[i] & 0xff);
            }
            return new UUID(mostSigBits, leastSigBits);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    @NotNull
    protected static byte[] generateUniqueBytes() {
        long timestamp = System.currentTimeMillis();
        byte[] timestampBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            timestampBytes[i] = (byte) (timestamp >>> (i * 8));
        }

        byte[] randomBytes = new byte[8];
        new SecureRandom().nextBytes(randomBytes);

        byte[] uniqueBytes = new byte[16];
        System.arraycopy(timestampBytes, 0, uniqueBytes, 0, 8);
        System.arraycopy(randomBytes, 0, uniqueBytes, 8, 8);

        return uniqueBytes;
    }
    protected static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
