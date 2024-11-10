package com.quevent.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final PrivateKey privateKey; // private key for signing
    private final PublicKey publicKey;   // public key for verification
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1hr
    private final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24hrs

    public JwtUtil() throws Exception {
        // Load private and public keys from strings
        this.privateKey = KeyUtil.getPrivateKeyFromString("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwpu4VW6dxSzdc3MQZyPgaoTCjBqTnDhvrdobNTZnEkK9E95KlNwCJJk7EbiuxigzMjjC/hWZdc9sdjxBk1LfOlqAxRJlnNlt6raWv+pYngp1OSNk7Xit/wgMKwdkkdGZqTYNNHxRVu/EQ7cVATsmMS3FVsoXGmTVtiLecxumLpA7mb4QEK+K1mdK959YKvsqy7VCD/7fEhJH68ckuf/QxOWMfGzdgIHrqzrUlDV60bGMjAxNgqL9QmE940nYj2Wp+uCoa6v0afcqI2rjwT6RZUNpTUsilWWMnA/4OBaj2Yps4COGQA0AksK7qeqvps9shV14AIimquIFsr4Wfg4bjAgMBAAECggEAQ33TssdMxwVUxp72MV/OIuZ43aB5Omnykti7Eg9W83nP7QqltYe5Ja1EJNpuySsUTsb4qwO9o3DTQ4zYV53KCB0WYRxze7KR6uITTpPv8HJ/xVwh2wPUnMA0R1TesIXkZzLsOnfoXgUrSit91F8FtBtS1soZQFJizlGQzU5WBmpqIKn3LfkDRtbJscpPJsKWuy10NfkLuLsP5R4nzMKCQlq/6tKeT2pEWQSQnmX7NJuliAnlQYJRNnDRRCyfo3VwX+shqEb75nN3AOUBUDecMTa4Ix2txRrfEnDvzmQI0ke4BE5B1zvRBUc4tcqD/8pDdEA4qqM2hOEt+rFbNCoDeQKBgQDI7d6Bbqw11+FfJ3a62LWq2mjrBX4fAuhxYnxKvcXj8tkA9+2Mjtn/4BUrdWutzBjcyPZ+eDkSMxbjD8JI3bIL4ZOVk+9VVblju9wAXYdseeFGodNn3HW2gKy7uOmvW+jcRT18cYKbki68qYPHCsZUpbEMWKpgdyil838Dq8uanwKBgQDhEapgp/YeSxDqLkTYz1sTSwOxMEzDDjNQbpnnEFyqo3Du8U1pXN5cf+8lcIsHQ5kOoZ+Ufq7affgbdNpHDumWXKeV1O9SxYpdfkJuyLw2Bie9OaH3mhZabL0MFyxxnDAMQy70jrxEVRQ7bVrd749HDDiYZVRrWpm2+Y8ptpDxPQKBgQCpaW4W4ZKPn2KlkSeZaMjbsmWN60aorRIBL9GuTw3AIWz4VRJUVcnW5T/UMZ/FnPVFiqfSgFlWT/83GX1tZIcPX3XJQz8jSzERtz5KjJF3U9gubplJ0+boJtv9N27I7QyMVFEAUNCXjQydA6xZMWuByWZbC5Mz3KRIL+Li3IHB7wKBgQCF2HmYLYqiDUYr3gQ3Y2joGk5By1V7EjDV/lAsJ4DUQYphrsK4i+/41mQjwcAMO9kD6H38UzIs4Yjw2xcL9wRW1RccdZs1gCB+5R0gwOOcxSBQ2btGgi4U7GGolQdrMNUqFbIQ4el4QZhclvXY0iREvjHR/0Cq7yBGvjazOc0MiQKBgH9C9wVUDmyZkC6NJEW2FSgNz45dbf9mtL/C+gUS0BbJevQWRAbHxudqVdi6xRwoxCeOX88kbgfDtzVqbPt7PF4pdzuClI5HY7zDVySa0CtI1LPYLANPGWUNC7NGNZMa4dAnoKlIU7YGYJdwr3GgEEcnZHR/AdLBlP9RCpschuxP");
        this.publicKey = KeyUtil.getPublicKeyFromString("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsKbuFVuncUs3XNzEGcj4GqEwowak5w4b63aGzU2ZxJCvRPeSpTcAiSZOxG4rsYoMzI4wv4VmXXPbHY8QZNS3zpagMUSZZzZbeq2lr/qWJ4KdTkjZO14rf8IDCsHZJHRmak2DTR8UVbvxEO3FQE7JjEtxVbKFxpk1bYi3nMbpi6QO5m+EBCvitZnSvefWCr7Ksu1Qg/+3xISR+vHJLn/0MTljHxs3YCB66s61JQ1etGxjIwMTYKi/UJhPeNJ2I9lqfrgqGur9Gn3KiNq48E+kWVDaU1LIpVljJwP+DgWo9mKbOAjhkANAJLCu6nqr6bPbIVdeACIpqriBbK+Fn4OG4wIDAQAB");
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        System.out.println("Generated Token: " + createToken(claims, username, EXPIRATION_TIME));
        System.out.println("Generated Token Validation: " + validateToken(createToken(claims, username, EXPIRATION_TIME)));
        return createToken(claims, username, EXPIRATION_TIME);
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, REFRESH_EXPIRATION_TIME);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        // Create and sign the JWT with RS256 using private key
        System.out.println("PRIVATE KEY: " + privateKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256) // Use the private key for signing
                .compact();
    }

    // Method to extract claims from a JWT token
    public Claims extractAllClaims(String token) {
        // Use JwtParserBuilder (available in jjwt 0.11.x and later)
        return Jwts.parser()
                .setSigningKey(publicKey)  // Set public key for verification
                .build()
                .parseClaimsJws(token) // Parse and verify the JWT
                .getBody();
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Validate JWT token by checking expiration
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
