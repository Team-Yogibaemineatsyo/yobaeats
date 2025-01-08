package com.sparta.yobaeats.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    STORE_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 폐업 처리된 가게입니다."),
    STORE_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "가게는 최대 3개까지만 운영할 수 있습니다"),

    // 유저 관련 익셉션
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    USER_DELETED(HttpStatus.GONE, "탈퇴한 유저입니다."),
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
