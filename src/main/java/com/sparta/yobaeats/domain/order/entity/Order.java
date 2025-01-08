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

    // 병합 후 주석풀기
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;


    // 병합 후 주석풀기
//    @ManyToOne // 주문과 가게 간의 관계 설정
//    @JoinColumn(name = "store_id", nullable = false)
//    private Store store; // Store 객체 참조

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Enumerated(EnumType.STRING) // Enum을 DB에 저장할 때 문자열로 저장
    @Column(name = "status", nullable = false)
    private Status status;

    @Builder
    public Order(Long id, Long storeId, Menu menu, Status status) {
        this.id = id;
        this.storeId = storeId;
        this.menu = menu;
        this.status = status != null ? status : Status.ORDER_REQUESTED;
    }

//    @Builder
//    public Order(Long id, User user, Store store, Menu menu, String status) {
//        this.id = id;
//        this.user = user;
//        this.store = store;
//        this.menu = menu;
//        this.status = status != null ? status : Status.ORDER_REQUESTED;
//    }

    public enum Status {
        ORDER_REQUESTED, // 주문 요청
        ORDER_ACCEPTED, // 주문 수락
        COOKING_COMPLETE, // 조리 완료
        DELIVERING, // 배달중
        DELIVERED; // 배달 완료
    }

    // 주문 상태 변경 메서드
    public void changeStatus(Status newStatus) {
        this.status = newStatus;
    }

}
