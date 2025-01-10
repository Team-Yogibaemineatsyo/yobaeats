package com.sparta.yobaeats.global.security.dto.request;

public record AuthLoginRequest(
        String email,
        String password
) {
}
