package com.sparta.yobaeats.domain.order.entity;

public enum OrderStatus {
    PENDING,             // 대기중(기본값)
    ORDER_REQUESTED,     // 주문 요청
    ORDER_ACCEPTED,      // 주문 수락
    COOKING_COMPLETE,    // 조리 완료
    DELIVERING,          // 배달 중
    DELIVERED;           // 배달 완료

    /**
     * 주어진 새로운 상태로의 전환이 가능한지 여부를 체크하는 메서드.
     * 상태 전환은 다음과 같은 규칙을 따릅니다:
     *     '대기중' -> '주문 요청됨'
     *     '주문 요청됨' -> '주문 수락됨'
     *     '주문 수락됨' -> '조리 완료됨'
     *     '조리 완료됨' -> '배달 중'
     *     '배달 중' -> '배달 완료됨'
     *     이 순서를 따라서만 상태 변경이 가능합니다.
     *
     * @return 전환이 가능한 상태를 반환하며, 불가능한 경우 IllegalStateException을 던집니다.
     */
    public OrderStatus nextStatus() {
        // 불가능한 전환 시 예외 발생
        return switch (this) {
            case PENDING -> ORDER_REQUESTED; // '대기중' -> '주문 요청됨'
            case ORDER_REQUESTED -> ORDER_ACCEPTED; // '주문 요청됨' -> '주문 수락됨'
            case ORDER_ACCEPTED -> COOKING_COMPLETE; // '주문 수락됨' -> '조리 완료됨'
            case COOKING_COMPLETE -> DELIVERING; // '조리 완료됨' -> '배달 중'
            case DELIVERING -> DELIVERED; // '배달 중' -> '배달 완료됨'
            default -> throw new IllegalStateException("주문 상태를 변경할 수 없습니다."); // 불가능한 전환 시 예외 발생
        };
    }
}
