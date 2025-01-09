package com.sparta.yobaeats.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDeleteReq(
    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    String password
) {
}
