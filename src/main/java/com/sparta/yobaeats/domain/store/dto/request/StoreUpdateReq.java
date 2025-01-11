package com.sparta.yobaeats.domain.store.dto.request;

import com.sparta.yobaeats.domain.store.dto.StoreValidationMessage;
import com.sparta.yobaeats.global.annotation.AtLeastOneNotNull;
import com.sparta.yobaeats.global.annotation.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

@AtLeastOneNotNull(message = ValidationMessage.AT_LEAST_ON_NOT_NULL_DEFAULT_MESSAGE)
public record StoreUpdateReq(
        @Size(
                max = StoreValidationMessage.NAME_MAX,
                message = StoreValidationMessage.NAME_MAX_MESSAGE
        )
        String storeName,

        LocalTime openAt,
        LocalTime closeAt,

        @Min(
                value = StoreValidationMessage.MIN_ORDER_PRICE_MIN,
                message = StoreValidationMessage.MIN_ORDER_PRICE_MIN_MESSAGE
        )
        Integer minOrderPrice
) {
}
