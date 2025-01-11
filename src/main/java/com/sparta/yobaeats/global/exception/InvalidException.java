package com.sparta.yobaeats.global.exception;

import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class InvalidException extends CustomRuntimeException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
