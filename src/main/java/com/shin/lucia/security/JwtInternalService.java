package com.shin.lucia.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtInternalService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private String internalToken;

    @PostConstruct
    public void init() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        internalToken = Jwts.builder()
                .setSubject("internal-service")
                .claim("role", "SYSTEM")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getToken() {
        return "Bearer " + internalToken;
    }
}
