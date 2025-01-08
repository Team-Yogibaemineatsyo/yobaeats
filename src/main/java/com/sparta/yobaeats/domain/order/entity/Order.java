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

import java.time.LocalDateTime;

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
    @JoinColumn(name = "user_id", nullable = false) // 유저와의 관계를 설정합니다.
    private User user; // User 엔티티 참조

    @ManyToOne // 하나의 주문은 하나의 가게에 속합니다.
    @JoinColumn(name = "store_id", nullable = false) // 외래 키 설정
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false) // 외래 키 설정
    private Menu menu;

    @Column(name = "status", nullable = false)
    private String status;

    @Builder
    public Order(Long id, User user, Store store, Menu menu, String status) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.status = status;
    }
}
