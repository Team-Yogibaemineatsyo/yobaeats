package com.sparta.yobaeats.domain.auth.dto.request;

import com.sparta.yobaeats.domain.user.dto.UserValidationMessage;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthSignupRequest(
        @NotBlank(message = UserValidationMessage.EMAIL_BLANK_MESSAGE)
        @Pattern(
                regexp = UserValidationMessage.EMAIL_REG,
                message = UserValidationMessage.INVALID_EMAIL_MESSAGE
        )
        String email,

        @NotBlank(message = UserValidationMessage.PASSWORD_BLANK_MESSAGE)
        @Pattern(
                regexp = UserValidationMessage.PASSWORD_REG,
                message = UserValidationMessage.INVALID_PASSWORD_MESSAGE
        )
        String password,

        @NotBlank(message = UserValidationMessage.NICKNAME_BLANK_MESSAGE)
        @Size(
                min = UserValidationMessage.NICKNAME_MIN,
                max = UserValidationMessage.NICKNAME_MAX,
                message = UserValidationMessage.NICKNAME_RANGE_MESSAGE
        )
        String nickName,

        @NotBlank(message = UserValidationMessage.ROLE_BLANK_MESSAGE)
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
