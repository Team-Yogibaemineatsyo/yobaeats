package com.sparta.yobaeats.global.security.handler;

import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.util.SecurityResponseMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityResponseMapper securityResponseMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("Security Authorization error: Access denied");

        ErrorCode error = ErrorCode.AUTHORIZATION_EXCEPTION;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getStatus().value());
        response.getWriter().write(securityResponseMapper.build(error));
//        throw new SecurityAccessDeniedException(ErrorCode.AUTHORIZATION_EXCEPTION);
    }
}
