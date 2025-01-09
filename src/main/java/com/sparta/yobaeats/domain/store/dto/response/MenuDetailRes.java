package com.sparta.yobaeats.domain.store.dto.response;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MenuDetailRes(
        Long menuId,
        String menuName,
        Integer menuPrice,
        String description
) {

    public static MenuDetailRes from(Menu menu) {
        return MenuDetailRes.builder()
                .menuId(menu.getId())
                .menuName(menu.getMenuName())
                .menuPrice(menu.getMenuPrice())
                .description(menu.getDescription())
                .build();
    }
}
