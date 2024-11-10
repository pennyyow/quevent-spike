package com.quevent.backend.util;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {
    public static RSAPublicKey getPublicKeyFromString(String publicKeyStr) throws Exception {
        // Remove the first and last lines
        String publicKeyPEM = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        // Decode the Base64 encoded string
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        System.out.println("Public Key (Base64 Decoded): " + new String(encoded));

        // Generate the public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    // Load RSA Private Key from a PEM-encoded string
    public static RSAPrivateKey getPrivateKeyFromString(String privateKeyString) {
        try {
            // Remove the "BEGIN" and "END" lines and decode the key
            String privateKeyPEM = privateKeyString.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
            byte[] encoded = java.util.Base64.getDecoder().decode(privateKeyPEM);

            System.out.println("Private Key (Base64 Decoded): " + new String(encoded));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key from string", e);
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048); // Key size
        return keyPairGen.generateKeyPair();
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Print public key in Base64 format
        System.out.println("Private Key: " + java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("Public Key: " + java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }
}
