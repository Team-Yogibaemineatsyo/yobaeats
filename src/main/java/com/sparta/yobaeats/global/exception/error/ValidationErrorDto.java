package com.sparta.yobaeats.global.exception.error;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationErrorDto {

    private List<String> errorCodes;

    public static ValidationErrorDto fail(List<String> errorCode) {
        return new ValidationErrorDto(errorCode);
    }
}
