package com.sparta.yobaeats.global.security.handler;

import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.util.SecurityResponseMapper;
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

    private final SecurityResponseMapper securityResponseMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("Authentication error: wrong password");

        response.setContentType("application/json;charset=UTF-8");
        ErrorCode errorCode = ErrorCode.LOGIN_FAILED_EXCEPTION;
        response.setStatus(errorCode.getStatus().value());
//        response.getWriter().write(securityResponseMapper.build(errorCode));
        response.getWriter().write(exception.getMessage());
    }
}
