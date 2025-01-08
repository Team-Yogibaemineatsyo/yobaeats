package com.sparta.yobaeats.domain.store.entity;

import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double starRate;

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Store(
            Long id,
            String name,
            Integer minOrderPrice,
            Double starRate,
            LocalTime openAt,
            LocalTime closeAt,
            boolean isDeleted,
            User user
    ) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.starRate = starRate;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.isDeleted = isDeleted;
        this.user = user;
    }
}
