package com.sparta.yobaeats.global.exception;

import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class NotFoundException extends CustomRuntimeException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
