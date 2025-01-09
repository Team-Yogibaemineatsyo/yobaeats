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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
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

    public enum Status {
        ORDER_REQUESTED, // 주문 요청
        ORDER_ACCEPTED, // 주문 수락
        COOKING_COMPLETE, // 조리 완료
        DELIVERING, // 배달중
        DELIVERED; // 배달 완료
    }

    /**
     * 주문 상태를 변경하는 메서드
     *
     * 이 메서드는 주문의 상태를 새로운 상태로 변경합니다.
     *
     * @param newStatus 새로운 주문 상태
     *                  - 가능한 상태는 ORDER_REQUESTED, ORDER_ACCEPTED,
     *                    COOKING_COMPLETE, DELIVERING, DELIVERED입니다.
     */
    public void changeStatus(Status newStatus) {
        this.status = newStatus;
    }
}
