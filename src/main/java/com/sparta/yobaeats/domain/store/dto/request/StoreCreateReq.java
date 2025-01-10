package com.sparta.yobaeats.domain.store.dto.request;

import com.sparta.yobaeats.domain.store.dto.StoreValidationMessage;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record StoreCreateReq(
        @Size(max = StoreValidationMessage.NAME_MAX, message = StoreValidationMessage.NAME_MAX_MESSAGE)
        @NotBlank(message = StoreValidationMessage.NAME_BLANK_MESSAGE)
        String storeName,

        @NotNull(message = StoreValidationMessage.OPEN_AT_NULL_MESSAGE)
        LocalTime openAt,

        @NotNull(message = StoreValidationMessage.CLOSE_AT_NULL_MESSAGE)
        LocalTime closeAt,

        @Min(value = StoreValidationMessage.MIN_ORDER_PRICE_MIN, message = StoreValidationMessage.MIN_ORDER_PRICE_MIN_MESSAGE)
        @NotNull(message = StoreValidationMessage.MIN_ORDER_PRICE_NULL_MESSAGE)
        Integer minOrderPrice
) {

    public Store toEntity(User user) {
        return Store.builder()
                .name(this.storeName)
                .openAt(this.openAt)
                .closeAt(this.closeAt)
                .minOrderPrice(this.minOrderPrice)
                .user(user)
                .build();
    }
}
