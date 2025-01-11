package com.sparta.yobaeats.domain.order.dto.request;

import com.sparta.yobaeats.domain.order.entity.OrderStatus;

public record OrderUpdateReq(

        /**
         * 주문 상태
         *
         * - 주문의 진행 상태를 나타내는 Enum 값입니다.
         * - 가능한 상태:
         *   - PENDING: 주문 대기 상태(기본값)
         *   - ORDER_REQUESTED: 주문이 요청된 상태
         *   - ORDER_ACCEPTED: 가게에서 주문을 수락한 상태
         *   - COOKING_COMPLETE: 가게에서 조리가 완료된 상태
         *   - DELIVERING: 배달원이 음식을 배달 중인 상태
         *   - DELIVERED: 배달이 완료된 상태
         */
        OrderStatus orderStatus
) {
}