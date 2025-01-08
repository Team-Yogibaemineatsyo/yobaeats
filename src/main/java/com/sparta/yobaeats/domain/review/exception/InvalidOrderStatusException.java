package com.sparta.yobaeats.domain.review.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class InvalidOrderStatusException extends CustomRuntimeException {
    public InvalidOrderStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
