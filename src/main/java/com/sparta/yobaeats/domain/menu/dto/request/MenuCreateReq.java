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
}
