package com.sparta.yobaeats.domain.auth.dto.request;

public record AuthLoginRequest(
        String email,
        String password
) {
}
