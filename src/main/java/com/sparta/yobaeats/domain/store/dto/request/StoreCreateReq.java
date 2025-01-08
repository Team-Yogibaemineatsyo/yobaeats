package com.sparta.yobaeats.domain.store.dto.request;

import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record StoreCreateReq(
        @NotBlank
        String storeName,

        @NotBlank
        LocalTime openAt,

        @NotBlank
        LocalTime closeAt,

        @NotBlank
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
