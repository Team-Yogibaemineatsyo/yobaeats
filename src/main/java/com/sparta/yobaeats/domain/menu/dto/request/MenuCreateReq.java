package com.sparta.yobaeats.domain.menu.dto.request;

import com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MenuCreateReq(
        @NotNull(message = MenuValidationMessage.STOREID_NOTNULL_MESSAGE)
        Long storeId,

        @NotNull(message = MenuValidationMessage.MENUINFO_NOTNULL_MESSAGE)
        List<MenuInfoCreateReq> menus
) {
    /**
     * Store 엔티티를 사용하여 Menu 엔티티 리스트를 생성합니다.
     *
     * @param store Store 엔티티
     * @return 생성된 Menu 엔티티 리스트
     */
    public List<Menu> toEntities(Store store) {
        return menus.stream()
                .map(menuInfoCreateReq -> menuInfoCreateReq.toEntity(store))
                .toList();
    }
}
