package com.sparta.yobaeats.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.exception.error.ErrorResponse;
import com.sparta.yobaeats.global.security.exception.SecurityAuthenticationFailedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Security Authentication error: need to login");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorCode.AUTHENTICATION_FAILED_EXCEPTION.getStatus().value());
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new ErrorResponse(
                                ErrorCode.AUTHENTICATION_FAILED_EXCEPTION.getStatus(),
                                ErrorCode.AUTHENTICATION_FAILED_EXCEPTION.getMessage()
                        )
                )
        );
//        throw new SecurityAuthenticationFailedException(ErrorCode.AUTHENTICATION_FAILED_EXCEPTION);
    }
}
