package com.sparta.yobaeats.domain.menu.dto;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;

/**
 * 메뉴 생성 요청 데이터를 담는 DTO 클래스
 */
public record MenuCreateReq(

        @NotBlank
        Long storeId,

        @NotBlank
        String menuName,

        @NotBlank
        Integer menuPrice,

        @NotBlank
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
