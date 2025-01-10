package com.sparta.yobaeats.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.exception.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityResponseMapper {

    private final ObjectMapper objectMapper;

    public String build(ErrorCode errorCode) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                new ErrorResponse(
                        errorCode.getStatus(),
                        errorCode.getMessage()
                )
        );
    }
}
