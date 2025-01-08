package com.sparta.yobaeats.domain.auth.controller;

import com.sparta.yobaeats.domain.auth.dto.AuthLoginResponse;
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
    public ResponseEntity<Void> signup () {
        authService.signup();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login() {
        authService.login();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout () {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
