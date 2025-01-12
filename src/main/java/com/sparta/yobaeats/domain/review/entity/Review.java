package com.sparta.yobaeats.domain.review.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.reply.entity.Reply;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int star;

    @OneToOne(mappedBy = "review")
    private Reply reply;

    @Column
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Builder
    public Review(
            Long id,
            User user,
            Store store,
            Order order,
            String content,
            int star,
            Reply reply

    ) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.order = order;
        this.content = content;
        this.star = star;
        this.reply = reply;
    }

    public void softDelete() {
        this.isDeleted = true;
        if (this.reply != null) {
            this.getReply().softDelete();
        }
    }
}