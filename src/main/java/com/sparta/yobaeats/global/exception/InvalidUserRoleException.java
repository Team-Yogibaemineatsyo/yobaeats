package com.sparta.yobaeats.global.exception;

import com.sparta.yobaeats.global.exception.error.ErrorCode;

public class InvalidUserRoleException extends CustomRuntimeException{
    public InvalidUserRoleException(ErrorCode errorCode){
        super(errorCode);
    }
}
