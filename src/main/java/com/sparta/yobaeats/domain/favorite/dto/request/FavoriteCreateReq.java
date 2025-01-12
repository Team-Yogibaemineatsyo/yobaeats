package com.sparta.yobaeats.domain.favorite.dto.request;

import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import jakarta.validation.constraints.NotNull;

public record FavoriteCreateReq(
        @NotNull
        Long storeId
) {

    public Favorite to(Long userId) {
        return Favorite.builder()
                .userId(userId)
                .storeId(this.storeId)
                .build();
    }
}