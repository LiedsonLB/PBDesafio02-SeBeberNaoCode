package com.sebebernaocode.authorization.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Slf4j
@Service
public class TokenService {

    private static final long EXPIRE_DAYS = 0;
    private static final long EXPIRE_HOURS = 1;
    private static final long EXPIRE_MINUTES = 0;

    @Value("${api.security.token.secret}")
    private String secretKey;

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime
                .plusMinutes(EXPIRE_MINUTES)
                .plusHours(EXPIRE_HOURS)
                .plusDays(EXPIRE_DAYS);

        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String createToken(String email, Set<String> roles) {
        Date issueAt = new Date();
        Date limit = toExpireDate(issueAt);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(email)
                .issuedAt(issueAt)
                .expiration(limit)
                .signWith(generateKey())
                .claim("role", roles)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token))
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            log.warn("Invalid token: {}", e.getMessage());
        }

        return null;
    }

    public boolean isInvalidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
        }
        return false;
    }

    private static String refactorToken(String token) {
        if (token.contains("Bearer "))
            return token.replace("Bearer ", "");

        return token;
    }
}
