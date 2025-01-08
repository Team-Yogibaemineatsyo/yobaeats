package com.sparta.yobaeats.domain.review.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class InvalidStarRangeException extends CustomRuntimeException {
    public InvalidStarRangeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
