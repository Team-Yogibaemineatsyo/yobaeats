package com.sparta.yobaeats.domain.cart.dto.request;

import com.sparta.yobaeats.domain.cart.dto.CartValidationMessage;
import jakarta.validation.constraints.NotNull;

public record CartItemQuantityUpdateReq(
        @NotNull(message = CartValidationMessage.CART_ITEM_INCREASE_NOT_NULL_MESSAGE)
        Boolean increase
) {
}
