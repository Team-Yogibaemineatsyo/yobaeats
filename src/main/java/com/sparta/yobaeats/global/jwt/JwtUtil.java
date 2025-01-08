package com.sparta.yobaeats.global.jwt;

import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Long EXPIRATION_TIME = 60 * 60 * 60L;
    private static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String generateToken(Long id, UserRole role) {
        return BEARER_PREFIX + Jwts.builder()
                .claim("id", id)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public User getUserFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return User.builder()
//                .id(claims.get("id", Long.class))
                .role(claims.get("role", UserRole.class))
                .build();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
