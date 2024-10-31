package com.quevent.backend.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String[] args) {
        String secretKey = generateKey();
        System.out.println("Generated Secret Key: " + secretKey);
    }
}
