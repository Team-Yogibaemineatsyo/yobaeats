package com.sparta.yobaeats.domain.auth.dto.request;

import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.enums.UserRole;

public record AuthSignupRequest(
        String email,
        String password,
        String nickName,
        String role
) {
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickName(this.nickName)
                .role(UserRole.of(this.role))
                .build();
    }
}
