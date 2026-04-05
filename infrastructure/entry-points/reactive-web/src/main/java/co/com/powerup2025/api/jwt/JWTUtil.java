package co.com.powerup2025.api.jwt;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final String issuer;

    public JWTUtil(@Value("${security.jwt.secret}") String secretKeyString,
                   @Value("${security.jwt.issuer}") String issuer) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
    }


    public String createToken(String subject, String role) {
        return Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .claim("authorities", List.of(role))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(560, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        Claims claims = parseToken(token);
        return claims.getExpiration().after(Date.from(Instant.now())) &&
                claims.getSubject().equals(expectedUsername) &&
                claims.getIssuer().equals(issuer); // 👈 Validación cruzada del issuer
    }

    public List<String> getRoles(String token) {
        return parseToken(token).get("authorities", List.class);
    }

    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }
}


