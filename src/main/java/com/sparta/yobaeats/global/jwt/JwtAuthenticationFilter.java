package com.sparta.yobaeats.global.jwt;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveHeader(request);

        if (StringUtils.hasText(token) && jwtUtil.isValid(token)) {
            User user = jwtUtil.getUserFromToken(token);
            UserDetailsCustom userDetails = new UserDetailsCustom(user);

            Authentication authToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveHeader(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authorization) || !jwtUtil.isStartsWithBearer(authorization)) {
            return null;
        }

        return authorization.split(" ")[1];
    }
}
