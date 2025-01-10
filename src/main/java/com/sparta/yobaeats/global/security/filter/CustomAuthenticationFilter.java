package com.sparta.yobaeats.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.domain.auth.dto.request.AuthLoginRequest;
import com.sparta.yobaeats.global.security.authentication.CustomAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    private static final String LOGIN_HTTP_METHOD = "POST";
    private static final String LOGIN_URL = "/api/auth/login";

    public CustomAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(LOGIN_URL, LOGIN_HTTP_METHOD)); // check login request path
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("Attempting authentication");

        AuthLoginRequest authLoginRequest = objectMapper.readValue(request.getReader(), AuthLoginRequest.class);

        validateAuthLoginRequest(authLoginRequest);

        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        CustomAuthenticationToken token = new CustomAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(token);
    }

    private void validateAuthLoginRequest(AuthLoginRequest authLoginRequest) {
        // null check
        if (!StringUtils.hasText(authLoginRequest.email()) || !StringUtils.hasText(authLoginRequest.password())) {
            throw new IllegalArgumentException("Email and password are required");
        }

        // TODO 추가 validation 필요
    }
}
