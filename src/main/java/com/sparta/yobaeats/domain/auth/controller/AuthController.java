package com.sparta.yobaeats.domain.auth.controller;

import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.auth.service.AuthService;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestBody @Valid AuthSignupRequest authSignupRequest
    ) {
        Long userId = authService.signup(authSignupRequest);
        URI uri = UriBuilderUtil.create("/api/users/{userId}", userId);

        return ResponseEntity.created(uri).build();
    }
}
