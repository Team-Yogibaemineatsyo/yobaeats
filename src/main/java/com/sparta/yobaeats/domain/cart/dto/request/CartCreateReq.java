package com.sparta.yobaeats.domain.cart.dto.request;

import com.sparta.yobaeats.domain.cart.dto.CartValidationMessage;
import com.sparta.yobaeats.domain.cart.entity.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartCreateReq(
        @NotNull(message = CartValidationMessage.CART_ITEM_MENU_ID_NOT_NULL_MESSAGE)
        Long menuId,

        @Min(
                value = CartValidationMessage.CART_ITEM_QUANTITY_MIN,
                message = CartValidationMessage.CART_ITEM_QUANTITY_MIN_MESSAGE
        )
        @NotNull(message = CartValidationMessage.CART_ITEM_QUANTITY_NOT_NULL_MESSAGE)
        Integer quantity
) {

    public CartItem toEntity() {
        return CartItem.builder()
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }
}
