package com.sparta.yobaeats.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) {
            return false;
        }

        Field[] fields = object.getClass().getDeclaredFields();

        // 필드들 중 하나라도 빈 값이 아니라면 true 반환
        for (Field field : fields) {
            field.setAccessible(true);  // private 필드 접근 허용

            try {
                Object value = field.get(object);

                if (value != null) {
                    if (value instanceof String && !((String) value).isBlank()) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                // 필드에 접근할 수 없을 경우
                log.warn("AtLeastOneNotNullValidator invalid error");
            }
        }

        return false;
    }
}
