package com.assureplus.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final String jwtSecret = "assureplus-secret-key-very-strong-very-long-1234567890";
    private final long jwtExpirationMs = 86400000; // 24h
    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public String generateToken(String username, UUID userId, String role, List<String> permissions) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId != null ? userId.toString() : null)
                .claim("role", role)
                .claim("permissions", permissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromJWT(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role");
    }

    public List<String> getPermissionsFromJWT(String token) {
        return (List<String>) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("permissions", List.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 