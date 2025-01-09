package com.sparta.yobaeats.domain.user.enums;

import com.sparta.yobaeats.domain.user.exception.InvalidUserRoleException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import java.util.Arrays;

public enum UserRole {
    ROLE_USER,
    ROLE_OWNER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(userRole -> userRole.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new InvalidUserRoleException(ErrorCode.INVALID_USER_ROLE));
    }
}
