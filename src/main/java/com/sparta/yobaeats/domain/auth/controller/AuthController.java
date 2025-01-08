package com.sparta.yobaeats.domain.auth.controller;

import com.sparta.yobaeats.domain.auth.dto.AuthLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<Void> signup () {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login() {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout () {
        return ResponseEntity.noContent().build();
    }
}
