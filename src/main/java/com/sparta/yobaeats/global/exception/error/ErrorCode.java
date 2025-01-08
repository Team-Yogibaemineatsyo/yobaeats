package com.sparta.yobaeats.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    STORE_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 폐업 처리된 가게입니다.");

    private final HttpStatus status;
    private final String message;
}
