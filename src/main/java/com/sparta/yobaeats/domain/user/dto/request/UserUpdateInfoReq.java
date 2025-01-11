package com.sparta.yobaeats.domain.user.dto.request;

import com.sparta.yobaeats.domain.user.dto.UserValidationMessage;
import com.sparta.yobaeats.global.annotation.AtLeastOneNotNull;
import com.sparta.yobaeats.global.annotation.ValidationMessage;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@AtLeastOneNotNull(message = ValidationMessage.AT_LEAST_ON_NOT_NULL_DEFAULT_MESSAGE)
public record UserUpdateInfoReq(
        @Pattern(
                regexp = UserValidationMessage.PASSWORD_REG,
                message = UserValidationMessage.INVALID_PASSWORD_MESSAGE
        )
        @Size(min = UserValidationMessage.PASSWORD_MIN, message = UserValidationMessage.PASSWORD_MIN_MESSAGE)
        String password,

        @Size(
                min = UserValidationMessage.NICKNAME_MIN,
                max = UserValidationMessage.NICKNAME_MAX,
                message = UserValidationMessage.NICKNAME_RANGE_MESSAGE
        )
        String nickName
) {

}