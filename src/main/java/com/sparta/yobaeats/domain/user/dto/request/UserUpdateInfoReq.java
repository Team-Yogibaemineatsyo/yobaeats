package com.sparta.yobaeats.domain.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateInfoReq(
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+]).{8,}$", message = "잘못된 비밀번호 형식입니다.")
    String password,


    @Size(min = 1, max = 10, message = "닉네임은 10자 내로 입력해주세요.")
    String nickName
) {
}

