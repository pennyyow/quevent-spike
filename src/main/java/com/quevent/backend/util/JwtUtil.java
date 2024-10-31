package com.quevent.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "ZgzPSFBvf21z4WgRxYz4hTmRUo/CEztVQPT1uMbhGEQ=";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1hr
    private final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24hrs

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, EXPIRATION_TIME);
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, REFRESH_EXPIRATION_TIME);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .claim("sub", subject) // can add additional claim ex. .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
}
