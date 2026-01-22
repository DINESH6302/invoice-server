package com.invoice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JWTService {

    private final Key key;

    @Autowired
    public JWTService(Key key) {
        this.key = key;
    }

    public String createAccessToken(Long userId, Duration ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .setIssuer("invoice-api")
                .claim("roles", List.of("ROLE_USER"))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId, String jti, Duration ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setId(jti)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .setIssuer("invoice-api")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseAndValidate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
