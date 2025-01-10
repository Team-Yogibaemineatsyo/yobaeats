package com.sparta.yobaeats.global.exception;

import com.sparta.yobaeats.global.exception.error.ErrorResponse;
import com.sparta.yobaeats.global.exception.error.ValidationErrorDto;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomRuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomRuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        return createResponseEntity(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return createResponseEntity(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorDto> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorCodes = bindingResult.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        ValidationErrorDto validationErrorDto = new ValidationErrorDto(errorCodes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return createResponseEntity(response);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return createResponseEntity(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return createResponseEntity(response);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorResponse response) {
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
