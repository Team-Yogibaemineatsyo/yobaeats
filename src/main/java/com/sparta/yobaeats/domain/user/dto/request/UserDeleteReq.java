package com.sparta.yobaeats.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDeleteReq(

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=(.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$-_+.,:?])).{8,}$", message = "잘못된 비밀번호 형식입니다.")
    String password
) {

}
