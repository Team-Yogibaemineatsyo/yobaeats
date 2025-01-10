package com.sparta.yobaeats.global.jwt;

import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
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

    public String generateTokenByAuthentication(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .claim("id", principal.getId().toString())
                .claim("role", authorities)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public boolean isStartsWithBearer(String token) {
        return token.startsWith(BEARER_PREFIX);
    }

    public boolean isValid(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .after(new Date());
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (Exception e) {
            log.error("유효하지 않는 JWT 토큰 입니다.");
        }
        return false;
    }

    public User getUserFromToken(String token) {
        Claims claims = getClaimsFromToken(token);

        return new User(
                Long.parseLong(claims.get("id", String.class)),
                UserRole.of(claims.get("role", String.class)));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
