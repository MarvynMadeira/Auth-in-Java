package com.authapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import com.authapp.model.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final long expirationHours = 1;

    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expirationHours, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String jwt) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        } catch(Exception e) {
            return null;
        }
    }
}
