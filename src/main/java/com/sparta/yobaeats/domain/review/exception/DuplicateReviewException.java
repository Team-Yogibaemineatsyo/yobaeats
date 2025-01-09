package com.sparta.yobaeats.domain.review.exception;

import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class DuplicateReviewException extends CustomRuntimeException {
    public DuplicateReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
