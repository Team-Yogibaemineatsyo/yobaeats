package com.sparta.yobaeats.domain.user.dto.request;

import com.sparta.yobaeats.domain.user.dto.UserValidationMessage;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateInfoReq(
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+]).{8,}$",
        message = UserValidationMessage.INVALID_PASSWORD_MESSAGE
    )
    String password,

    @Size(min = 1, max = 10, message = UserValidationMessage.NICKNAME_RANGE_MESSAGE)
    String nickName
) {
}

