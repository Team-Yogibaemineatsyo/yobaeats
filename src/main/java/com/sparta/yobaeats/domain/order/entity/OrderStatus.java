package com.sparta.yobaeats.domain.order.entity;

public enum OrderStatus {
    PENDING,             // 대기중(기본값)
    ORDER_REQUESTED,     // 주문 요청
    ORDER_ACCEPTED,      // 주문 수락
    COOKING_COMPLETE,    // 조리 완료
    DELIVERING,          // 배달 중
    DELIVERED;           // 배달 완료

    /**
     * 현재 주문 상태에서 다음 상태로 전환하는 메서드.
     * 이 메서드는 주문의 상태 전환 규칙을 따릅니다:
     *     '대기중' -> '주문 요청됨'
     *     '주문 요청됨' -> '주문 수락됨'
     *     '주문 수락됨' -> '조리 완료됨'
     *     '조리 완료됨' -> '배달 중'
     *     '배달 중' -> '배달 완료됨'
     *
     * 전환이 불가능한 경우 IllegalStateException이 발생합니다.
     *
     * @return 다음으로 전환 가능한 주문 상태
     * @throws IllegalStateException 전환이 불가능한 경우 발생
     */
    public OrderStatus nextStatus() {
        // 불가능한 전환 시 예외 발생
        return switch (this) {
            case PENDING -> ORDER_REQUESTED; // '대기중' -> '주문 요청됨'
            case ORDER_REQUESTED -> ORDER_ACCEPTED; // '주문 요청됨' -> '주문 수락됨'
            case ORDER_ACCEPTED -> COOKING_COMPLETE; // '주문 수락됨' -> '조리 완료됨'
            case COOKING_COMPLETE -> DELIVERING; // '조리 완료됨' -> '배달 중'
            case DELIVERING -> DELIVERED; // '배달 중' -> '배달 완료됨'
            default -> throw new IllegalStateException("주문 상태를 변경할 수 없습니다.");
        };
    }
}
