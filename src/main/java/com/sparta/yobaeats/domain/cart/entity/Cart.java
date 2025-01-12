package com.sparta.yobaeats.domain.cart.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "cart", timeToLive = 3600)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart implements Serializable {

    @Id
    private Long userId;
    private List<CartItem> items;

    @Builder
    public Cart(Long userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
    }
}