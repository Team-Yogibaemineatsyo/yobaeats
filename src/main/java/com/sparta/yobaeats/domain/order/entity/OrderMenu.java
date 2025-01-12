package com.sparta.yobaeats.domain.order.entity;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_id")
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder
    public OrderMenu(Long id, Integer quantity, Order order, Menu menu) {
        this.id = id;
        this.quantity = quantity;
        this.order = order;
        this.menu = menu;
    }
}
