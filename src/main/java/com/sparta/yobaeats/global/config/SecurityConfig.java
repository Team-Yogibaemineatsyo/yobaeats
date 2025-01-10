package com.sparta.yobaeats.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.global.jwt.JwtAuthenticationFilter;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import com.sparta.yobaeats.global.security.handler.SecurityAccessDeniedHandler;
import com.sparta.yobaeats.global.security.handler.SecurityAuthenticationEntryPoint;
import com.sparta.yobaeats.global.security.handler.SecurityAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler() {
        return new SecurityAuthenticationFailureHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션 stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 접근 권한
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // 모든 권한 허용
                .requestMatchers("/api/users/**").hasRole("USER")
                .anyRequest().hasRole("USER")) // 나머지 로그인한 사용자만 접근 가능

                // filter
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)

                // exception handler
                .exceptionHandling(handler ->
                        handler.authenticationEntryPoint(new SecurityAuthenticationEntryPoint()))
                .exceptionHandling(handler ->
                        handler.accessDeniedHandler(new SecurityAccessDeniedHandler(objectMapper)))
                .build();
    }
}
