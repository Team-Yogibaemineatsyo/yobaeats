package com.sparta.yobaeats.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateReq(

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "잘못된 이메일 형식입니다.")
    String email,

    @Pattern(regexp = "^(?=(.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$-_+.,:?])).{8,}$", message = "잘못된 비밀번호 형식입니다.")
    String password,

    @Size(min = 1, max = 10, message = "리뷰 내용은 1자 이상 10자 이하로 입력해주세요.")
    String nickName
) {
}

