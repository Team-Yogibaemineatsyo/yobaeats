package com.sparta.yobaeats.domain.cart.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CartReadDetailRes(
        List<CartItemInfoRes> cartItems,
        Integer cartTotalPrice
) {

    public static CartReadDetailRes from(List<CartItemInfoRes> items) {
        return CartReadDetailRes.builder()
                .cartItems(items)
                .cartTotalPrice(getTotalPrice(items))
                .build();
    }

    private static Integer getTotalPrice(List<CartItemInfoRes> items) {
        return items.stream().
                mapToInt(CartItemInfoRes::menuTotalPrice)
                .sum();
    }
}
