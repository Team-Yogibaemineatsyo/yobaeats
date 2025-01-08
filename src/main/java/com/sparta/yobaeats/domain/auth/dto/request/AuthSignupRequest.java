package com.sparta.yobaeats.domain.auth.dto.request;

public record AuthSignupRequest(
        String email,
        String password,
        String nickName,
        String role
) {
}
