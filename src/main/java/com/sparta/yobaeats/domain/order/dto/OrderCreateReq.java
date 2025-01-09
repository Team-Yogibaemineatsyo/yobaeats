package com.sparta.yobaeats.domain.order.dto;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;

/**
 * 주문 생성 요청 데이터를 담는 DTO 클래스
 *
 * 이 클래스는 주문 생성 API에서 클라이언트가 전송하는 요청 데이터를 나타냅니다.
 * 필요한 필드는 모두 불변(immutable)하도록 record로 정의되었습니다.
 */
public record OrderCreateReq(

        @NotBlank
        Long storeId,

        @NotBlank
        Long menuId
) {
    /**
     * OrderCreateReq를 사용하여 Order 엔티티 생성
     *
     * @param store 주문할 가게의 Store 엔티티
     * @param menu 주문할 메뉴의 Menu 엔티티
     * @return 생성된 Order 객체
     */
    public Order toEntity(Store store, Menu menu) {
        return Order.builder()
                .store(store)
                .menu(menu)
                .status(Order.Status.ORDER_REQUESTED)
                .build();
    }
}
