package com.sparta.yobaeats.domain.menu.dto.request;

import com.sparta.yobaeats.domain.menu.dto.MenuValidationMessage;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 메뉴 생성 요청 데이터를 담는 DTO 클래스
 */
public record MenuCreateReq(

        @NotBlank(message = MenuValidationMessage.STOREID_BLANK_MESSAGE)
        Long storeId,

        @Size(max = MenuValidationMessage.NAME_MAX, message = MenuValidationMessage.NAME_MAX_MESSAGE)
        @NotBlank(message = MenuValidationMessage.NAME_BLANK_MESSAGE)
        String menuName,

        @NotNull(message = "메뉴가격을 입력해주세요.")
        Integer menuPrice,

        @Size(max = MenuValidationMessage.DESCRIPTION_MAX, message = MenuValidationMessage.DESCRIPTION_MAX_MESSAGE)
        @NotBlank(message = MenuValidationMessage.DESCRIPTION_BLANK_MESSAGE)
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
