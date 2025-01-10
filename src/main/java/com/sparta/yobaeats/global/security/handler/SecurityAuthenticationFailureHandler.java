package com.sparta.yobaeats.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("Authentication error: wrong password");

        ErrorCode error = ErrorCode.LOGIN_FAILED_EXCEPTION;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
