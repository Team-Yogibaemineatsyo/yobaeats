package com.sparta.yobaeats.domain.cart.dto.request;

import com.sparta.yobaeats.domain.cart.entity.CartItem;
import jakarta.validation.constraints.NotNull;

public record CartCreateReq(
        @NotNull
        Long menuId,

        @NotNull
        Integer quantity
) {

    public CartItem toEntity() {
        return CartItem.builder()
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }
}
