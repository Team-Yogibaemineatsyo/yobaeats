package com.sparta.yobaeats.domain.menu.dto.response;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.order.entity.OrderMenu;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MenuOrderInfoRes(
        Long menuId,
        String menuName,
        Integer menuPrice,
        Integer menuQuantity
) {

    public static MenuOrderInfoRes from(OrderMenu orderMenu) {
        Menu menu = orderMenu.getMenu();

        return MenuOrderInfoRes.builder()
                .menuId(menu.getId())
                .menuName(orderMenu.getMenuNameHistory())
                .menuPrice(orderMenu.getMenuPriceHistory())
                .menuQuantity(orderMenu.getQuantity())
                .build();
    }
}
