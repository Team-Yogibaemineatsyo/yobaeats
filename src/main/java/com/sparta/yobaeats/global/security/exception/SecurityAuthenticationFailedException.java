package com.sparta.yobaeats.global.security.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class SecurityAuthenticationFailedException extends CustomRuntimeException {

    public SecurityAuthenticationFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
