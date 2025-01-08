package com.sparta.yobaeats.domain.auth.controller;

import com.sparta.yobaeats.domain.auth.dto.request.AuthLoginRequest;
import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.auth.dto.response.AuthLoginResponse;
import com.sparta.yobaeats.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(AuthSignupRequest authSignupRequest) {
        authService.signup(authSignupRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(AuthLoginRequest authLoginRequest) {
        authService.login(authLoginRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
