package com.sparta.yobaeats.domain.menu.dto.request;

import com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MenuInfoCreateReq(
        @Size(max = MenuValidationMessage.NAME_MAX, message = MenuValidationMessage.NAME_MAX_MESSAGE)
        @NotBlank(message = MenuValidationMessage.NAME_BLANK_MESSAGE)
        String menuName,

        @Min(value = MenuValidationMessage.MENU_PRICE_MIN, message = MenuValidationMessage.MENU_PRICE_MIN_MESSAGE)
        @NotNull(message = MenuValidationMessage.PRICE_NOTNULL_MESSAGE)
        Integer menuPrice,

        @Size(max = MenuValidationMessage.DESCRIPTION_MAX, message = MenuValidationMessage.DESCRIPTION_MAX_MESSAGE)
        @NotBlank(message = MenuValidationMessage.DESCRIPTION_BLANK_MESSAGE)
        String description
) {
    /**
     * MenuInfoCreateReq를 사용하여 Menu 엔티티를 생성합니다.
     *
     * @param store 메뉴가 속한 Store 엔티티
     * @return 생성된 Menu 객체
     */
    public Menu toEntity(Store store) {
        return Menu.builder()
                .store(store)
                .menuName(menuName)
                .menuPrice(menuPrice)
                .description(description)
                .build();
    }
}
