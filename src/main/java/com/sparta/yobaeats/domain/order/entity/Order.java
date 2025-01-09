package com.sparta.yobaeats.domain.order.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.entity.Store;
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
    private OrderStatus orderStatus;

    @Builder
    public Order(Long id, User user, Store store, Menu menu, OrderStatus orderStatus) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.orderStatus = orderStatus;
    }

    // 주문 상태 변경
    public void changeStatusToNext() {
        this.orderStatus = this.orderStatus.nextStatus();  // nextStatus 호출하여 상태 전환
    }
}
