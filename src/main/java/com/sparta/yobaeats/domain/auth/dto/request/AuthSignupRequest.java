package com.sparta.yobaeats.domain.auth.dto.request;

import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AuthSignupRequest(

        @NotBlank @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String email,

        @NotBlank @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")
        String password,

        @NotBlank @Length(min = 2, max = 15)
        String nickName,

        @NotBlank
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
