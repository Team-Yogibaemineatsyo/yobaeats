package com.sparta.yobaeats.global.security.handler;

import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.util.SecurityResponseMapper;
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

    private final SecurityResponseMapper securityResponseMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("Security Authentication error: need to login");

        response.setContentType("application/json;charset=UTF-8");
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED_EXCEPTION;
        response.setStatus(errorCode.getStatus().value());
        response.getWriter().write(securityResponseMapper.build(errorCode));
//        throw new SecurityAuthenticationFailedException(ErrorCode.AUTHENTICATION_FAILED_EXCEPTION);
    }
}
