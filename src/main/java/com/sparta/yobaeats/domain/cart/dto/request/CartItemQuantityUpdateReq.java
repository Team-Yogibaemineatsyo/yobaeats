package com.sparta.yobaeats.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartItemQuantityUpdateReq(
        @NotNull
        Boolean increase
) {
}
