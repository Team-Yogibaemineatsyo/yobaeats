package com.sparta.yobaeats.global.security.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class SecurityAccessDeniedException extends CustomRuntimeException {

    public SecurityAccessDeniedException(ErrorCode errorCode) {
      super(errorCode);
    }
}
