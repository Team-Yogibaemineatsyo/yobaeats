package com.sparta.yobaeats.domain.favorite.dto.request;

import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import com.sparta.yobaeats.domain.order.dto.OrderValidationMessage;
import com.sparta.yobaeats.domain.store.dto.StoreValidationMessage;
import jakarta.validation.constraints.NotNull;

public record FavoriteCreateReq(
        @NotNull(message = OrderValidationMessage.STORE_ID_NOTNULL_MESSAGE)
        Long storeId
) {

    public Favorite to(Long userId) {
        return Favorite.builder()
                .userId(userId)
                .storeId(this.storeId)
                .build();
    }
}