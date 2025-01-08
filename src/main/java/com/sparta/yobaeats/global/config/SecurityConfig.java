package com.sparta.yobaeats.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable());
        http.formLogin(auth -> auth.disable());
        http.httpBasic(auth -> auth.disable());
//
//        // 경로별 인가 작업
//        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/login", "/", "join").permitAll() // 모든 권한 허용
//                .requestMatchers("/admin").hasRole("ADMIN") // admin 권한 가진 유저만 접근 가능
//                .anyRequest().authenticated() // 나머지 요청들 인가 작업 : 로그인한 사용자만 접근 가능
//        );

        return http.build();
    }
}
