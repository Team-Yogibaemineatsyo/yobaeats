package com.sparta.yobaeats.global.jwt.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class JwtNotFoundException extends CustomRuntimeException {

    public JwtNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
