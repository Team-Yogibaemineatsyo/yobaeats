package com.sparta.yobaeats.global.exception;

import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class ConflictException extends CustomRuntimeException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
