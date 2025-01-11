package com.sparta.yobaeats.domain.menu.dto.request;

import com.sparta.yobaeats.global.annotation.AtLeastOneNotNull;
import com.sparta.yobaeats.global.annotation.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import static com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage.*;
import static com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage.MENU_PRICE_MIN_MESSAGE;

/**
 * 메뉴 수정 요청을 위한 DTO 클래스입니다.
 * 이 클래스는 최소 하나의 필드가 null이 아닌 경우에 대한 유효성 검사를 포함합니다.
 */
@AtLeastOneNotNull(message = ValidationMessage.AT_LEAST_ON_NOT_NULL_DEFAULT_MESSAGE)
public record MenuUpdateReq(
        /**
         * 수정할 메뉴의 이름.
         * 최대 길이는 {@link MenuValidationMessage#NAME_MAX}입니다.
         */
        @Size(max = NAME_MAX, message = NAME_MAX_MESSAGE)
        String menuName,

        /**
         * 수정할 메뉴의 가격.
         * 최소 값은 {@link MenuValidationMessage#MENU_PRICE_MIN}입니다.
         */
        @Min(value = MENU_PRICE_MIN, message = MENU_PRICE_MIN_MESSAGE)
        Integer menuPrice,

        /**
         * 수정할 메뉴의 설명.
         * 최대 길이는 {@link MenuValidationMessage#DESCRIPTION_MAX}입니다.
         */
        @Size(max = DESCRIPTION_MAX, message = DESCRIPTION_MAX_MESSAGE)
        String description
) {
}
