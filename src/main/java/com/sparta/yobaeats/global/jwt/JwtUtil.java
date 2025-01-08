package com.sparta.yobaeats.global.jwt;

import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Long EXPIRATION_TIME = 60 * 60 * 1000L;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(CHARSET), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String generateToken(Long id, UserRole role) {
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .claim("id", id)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public boolean isStartsWithBearer(String token) {
        return token.startsWith(BEARER_PREFIX);
    }

    public boolean isValidExpiration(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .after(new Date());
    }

    public User getUserFromToken(String token) {
        Claims claims = getClaimsFromToken(token);

        return new User(
                claims.get("id", Long.class),
                UserRole.of(claims.get("role", String.class)));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
