package com.sparta.yobaeats.domain.order.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> menus;

    @Enumerated(EnumType.STRING) // Enum을 DB에 저장할 때 문자열로 저장
    @Column(name = "status", nullable = false)
    @ColumnDefault("'ORDER_REQUESTED'")
    private OrderStatus orderStatus;

    @Builder
    public Order(
            Long id,
            Integer totalPrice,
            User user,
            Store store,
            List<OrderMenu> menus,
            OrderStatus orderStatus
    ) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.user = user;
        this.store = store;
        this.menus = menus != null ? menus : new ArrayList<>();
        this.orderStatus = orderStatus;
    }

    /**
     * 주문 상태를 다음 상태로 변경하는 메서드
     * <p>
     * 현재 주문 상태에 따라 다음 상태로 전환합니다.
     * 상태 전환은 OrderStatus Enum의 nextStatus() 메서드를 통해 처리됩니다.
     */
    public void changeStatusToNext() {
        this.orderStatus = this.orderStatus.nextStatus();
    }

    public void addOrderMenu(OrderMenu orderMenu) {
        this.menus.add(orderMenu);
    }

    public void addTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
