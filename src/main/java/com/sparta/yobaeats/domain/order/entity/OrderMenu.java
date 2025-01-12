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

    @Column(nullable = false)
    private String menuNameHistory; // 주문 시점의 메뉴 이름

    @Column(nullable = false)
    private Integer menuPriceHistory; // 주문 시점의 메뉴 가격

    @Builder
    public OrderMenu(
            Long id,
            Integer quantity,
            Order order,
            Menu menu,
            String menuNameHistory,
            Integer menuPriceHistory
    ) {
        this.id = id;
        this.quantity = quantity;
        this.order = order;
        this.menu = menu;
        this.menuNameHistory = menuNameHistory;
        this.menuPriceHistory = menuPriceHistory;
    }

    public Integer getMenuTotalPrice() {
        return this.quantity * this.menuPriceHistory;
    }
}
