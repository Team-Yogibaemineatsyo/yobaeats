package com.sparta.yobaeats.domain.user.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class InvalidUserRoleException extends CustomRuntimeException {

    public InvalidUserRoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
