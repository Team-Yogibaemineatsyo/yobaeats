package com.sparta.yobaeats.domain.review.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int star;

    @Builder
    public Review(
        Long id,
        User user,
        Store store,
        Order order,
        String contents,
        int star
    ) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.order = order;
        this.contents = contents;
        this.star = star;
    }
}
