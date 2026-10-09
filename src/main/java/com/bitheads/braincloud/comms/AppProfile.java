package com.bitheads.braincloud.comms;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.function.Function;

// Request signer for an app.
final class AppProfile implements Function<byte[], String> {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final byte[] a;
    private final byte[] b;

    private AppProfile(byte[] value) {
        a = new byte[value.length];
        RANDOM.nextBytes(a);
        b = new byte[value.length];
        for (int i = 0; i < value.length; ++i) {
            b[i] = (byte) (value[i] ^ a[i]);
        }
        Arrays.fill(value, (byte) 0);
    }

    // Null for an empty value: requests go unsigned, as before.
    static Function<byte[], String> fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new AppProfile(value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String apply(byte[] payload) {
        byte[] v = new byte[a.length];
        for (int i = 0; i < a.length; ++i) {
            v[i] = (byte) (a[i] ^ b[i]);
        }
        return sign(payload, v);
    }

    // Lowercase hex request signature; clears value.
    static String sign(byte[] payload, byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(payload);
            md.update(value);
            byte[] digest = md.digest();
            char[] out = new char[digest.length * 2];
            final char[] digits = "0123456789abcdef".toCharArray();
            for (int i = 0; i < digest.length; ++i) {
                out[i * 2] = digits[(digest[i] >> 4) & 0xf];
                out[i * 2 + 1] = digits[digest[i] & 0xf];
            }
            return new String(out);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } finally {
            Arrays.fill(value, (byte) 0);
        }
    }

    @Override
    public String toString() {
        return "AppProfile";
    }
}
