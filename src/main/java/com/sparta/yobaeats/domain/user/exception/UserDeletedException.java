package com.sparta.yobaeats.domain.user.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class UserDeletedException extends CustomRuntimeException {

    public UserDeletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
