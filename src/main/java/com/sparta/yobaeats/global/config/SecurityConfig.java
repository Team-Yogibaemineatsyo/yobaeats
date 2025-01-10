package com.sparta.yobaeats.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.global.jwt.JwtAuthenticationFilter;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import com.sparta.yobaeats.global.security.filter.CustomAuthenticationFilter;
import com.sparta.yobaeats.global.security.handler.SecurityAccessDeniedHandler;
import com.sparta.yobaeats.global.security.handler.SecurityAuthenticationEntryPoint;
import com.sparta.yobaeats.global.security.handler.SecurityAuthenticationFailureHandler;
import com.sparta.yobaeats.global.security.handler.SecurityAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;

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
//                .addFilterAfter(new CustomUsernamePasswordAuthenticationFilter(objectMapper), LogoutFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthenticationFilter(), JwtAuthenticationFilter.class)

                // exception handler
                .exceptionHandling(handler ->
                        handler.authenticationEntryPoint(new SecurityAuthenticationEntryPoint(objectMapper)))
                .exceptionHandling(handler ->
                        handler.accessDeniedHandler(new SecurityAccessDeniedHandler(objectMapper)))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new SecurityAuthenticationSuccessHandler(objectMapper, jwtUtil));
        filter.setAuthenticationFailureHandler(new SecurityAuthenticationFailureHandler(objectMapper));

        filter.setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(),
                        new HttpSessionSecurityContextRepository()
                ));

        return filter;
    }
}
