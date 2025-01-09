package com.sparta.yobaeats.domain.order.entity;

import com.sparta.yobaeats.domain.common.BaseEntity; // BaseEntity import
import com.sparta.yobaeats.domain.menu.entity.Menu; // Menu 엔티티 import
import com.sparta.yobaeats.domain.store.entity.Store; // Store 엔티티 import
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Enumerated(EnumType.STRING) // Enum을 DB에 저장할 때 문자열로 저장
    @Column(name = "status", nullable = false)
    private Status status;

    @Builder
    public Order(Long id, User user, Store store, Menu menu, Status status) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.status = status;
    }

    /**
     * 주문 상태를 나타내는 Enum.
     * <p>
     * 이 Enum은 주문의 상태를 나타내며, 각 상태 간의 전환 규칙을 정의합니다.
     * 상태 전환은 특정 순서를 따르며, 상태 변경이 가능한지 여부를 체크하는 메서드를 제공합니다.
     * </p>
     */
    public enum Status {
        ORDER_REQUESTED,     // 주문 요청
        ORDER_ACCEPTED,      // 주문 수락
        COOKING_COMPLETE,    // 조리 완료
        DELIVERING,          // 배달 중
        DELIVERED;           // 배달 완료

        /**
         * 주어진 새로운 상태로의 전환이 가능한지 여부를 체크하는 메서드.
         * 상태 전환은 다음과 같은 규칙을 따릅니다:
         *     '주문 요청됨' -> '주문 수락됨'
         *     '주문 수락됨' -> '조리 완료됨'
         *     '조리 완료됨' -> '배달 중'
         *     '배달 중' -> '배달 완료됨'

         *
         * @param newStatus 새로운 상태
         * @return 전환 가능하면 true, 불가능하면 false
         */
        public boolean canTransitionTo(Status newStatus) {
            switch (this) {
                case ORDER_REQUESTED:
                    return newStatus == ORDER_ACCEPTED; // '주문 요청됨' -> '주문 수락됨'
                case ORDER_ACCEPTED:
                    return newStatus == COOKING_COMPLETE; // '주문 수락됨' -> '조리 완료됨'
                case COOKING_COMPLETE:
                    return newStatus == DELIVERING; // '조리 완료됨' -> '배달 중'
                case DELIVERING:
                    return newStatus == DELIVERED; // '배달 중' -> '배달 완료됨'
                default:
                    return false; // 그 외의 상태는 전환 불가능
            }
        }
    }

    /**
     * 주문 상태를 변경하는 메서드.
     * 상태는 일정한 순서에 따라 변경될 수 있으며, 전환이 불가능한 상태에서는 예외가 발생합니다.
     *
     * @param newStatus 변경하려는 새로운 주문 상태
     * @throws IllegalStateException 상태 전환이 불가능한 경우 예외가 발생합니다.
     *                               예를 들어, 이미 '배달 완료됨' 상태에서 다른 상태로 변경을 시도하는 경우
     *                               예외가 발생합니다.
     */
    public void changeStatus(Status newStatus) {
        // 상태 전환이 가능한지 여부를 확인
        if (this.status.canTransitionTo(newStatus)) {
            this.status = newStatus; // 상태 변경
        } else {
            throw new IllegalStateException("상태 전환이 불가능한 상태입니다."); // 불가능한 전환 시 예외 발생
        }
    }
}