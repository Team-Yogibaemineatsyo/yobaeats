package com.sparta.yobaeats.domain.menu.dto.request;

import com.sparta.yobaeats.global.annotation.AtLeastOneNotNull;
import com.sparta.yobaeats.global.annotation.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import static com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage.*;
import static com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage.MENU_PRICE_MIN_MESSAGE;

@AtLeastOneNotNull(message = ValidationMessage.AT_LEAST_ON_NOT_NULL_DEFAULT_MESSAGE)
public record MenuUpdateReq(
        @Size(max = NAME_MAX, message = NAME_MAX_MESSAGE)
        String menuName,

        @Min(value = MENU_PRICE_MIN, message = MENU_PRICE_MIN_MESSAGE)
        Integer menuPrice,

        @Size(max = DESCRIPTION_MAX, message = DESCRIPTION_MAX_MESSAGE)
        String description
) {
}
