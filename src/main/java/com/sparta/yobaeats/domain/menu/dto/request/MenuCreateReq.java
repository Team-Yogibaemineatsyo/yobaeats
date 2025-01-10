package com.sparta.yobaeats.domain.menu.dto.request;

import com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.dto.StoreValidationMessage;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import jakarta.validation.constraints.*;

import static com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage.*;

public record MenuCreateReq(

        @NotNull(message = STOREID_NOTNULL_MESSAGE)
        Long storeId,

        @Size(max = NAME_MAX, message = NAME_MAX_MESSAGE)
        @NotBlank(message = NAME_BLANK_MESSAGE)
        String menuName,

        @Min(value = MENU_PRICE_MIN, message = MENU_PRICE_MIN_MESSAGE)
        @NotNull(message = PRICE_NOTNULL_MESSAGE)
        Integer menuPrice,

        @Size(max = DESCRIPTION_MAX, message = DESCRIPTION_MAX_MESSAGE)
        @NotBlank(message = DESCRIPTION_BLANK_MESSAGE)
        String description
) {
    /**
     * MenuCreateReq를 사용하여 Menu 엔티티 생성
     *
     * @param store 메뉴가 속한 Store 엔티티
     * @return Menu 객체
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
