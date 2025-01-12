package com.sparta.yobaeats.domain.cart.dto.response;

import com.sparta.yobaeats.domain.cart.entity.CartItem;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CartItemInfoRes(
        Long menuId,
        String menuName,
        Integer menuPrice,
        Integer menuQuantity,
        Integer menuTotalPrice
) {

    public static CartItemInfoRes of(CartItem item, Menu menu) {
        Integer menuPrice = menu.getMenuPrice();
        Integer menuQuantity = item.getQuantity();

        return CartItemInfoRes.builder()
                .menuId(menu.getId())
                .menuName(menu.getMenuName())
                .menuPrice(menuPrice)
                .menuQuantity(menuQuantity)
                .menuTotalPrice(menuPrice * menuQuantity)
                .build();
    }
}
