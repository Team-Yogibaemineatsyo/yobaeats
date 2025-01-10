package com.sparta.yobaeats.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.exception.error.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT error");

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(ErrorCode.JWT_TOKEN_ERROR.getStatus().value());
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            new ErrorResponse(
                                    ErrorCode.JWT_TOKEN_ERROR.getStatus(),
                                    ErrorCode.JWT_TOKEN_ERROR.getMessage()
                            )
                    )
            );
        }
    }
}
