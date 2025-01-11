package com.sparta.yobaeats.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 요청에 수정할 내용이 적어도 하나의 값은 존재하는 지 검사하는 어노테이션
 */
@Constraint(validatedBy = AtLeastOneNotNullValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AtLeastOneNotNull {
    String message() default "There is nothing to modify in the request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}