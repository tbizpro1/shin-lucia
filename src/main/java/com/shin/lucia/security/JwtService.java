package com.shin.lucia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject(); // "luisedu", por exemplo
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object authorities = claims.get("authorities");

        if (authorities instanceof List<?>) {
            return ((List<?>) authorities).stream()
                    .map(Object::toString)
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .collect(Collectors.toList());
        }

        if (authorities instanceof String) {
            String role = (String) authorities;
            return List.of(role.startsWith("ROLE_") ? role : "ROLE_" + role);
        }

        return List.of();
    }

    private Claims extractAllClaims(String token) {
        try {
            String rawToken = token.replace("Bearer ", "").trim();
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(rawToken)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido.");
        }
    }


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
