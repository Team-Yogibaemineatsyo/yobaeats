package com.sparta.yobaeats.domain.favorite.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "favorites",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "store_id"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Builder
    public Favorite(Long userId, Long storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }
}
