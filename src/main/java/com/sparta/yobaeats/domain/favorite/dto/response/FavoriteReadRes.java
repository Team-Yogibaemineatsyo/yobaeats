package com.sparta.yobaeats.domain.favorite.dto.response;

import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import lombok.Builder;

@Builder
public record FavoriteReadRes(
    Long userId,
    Long storeId
) {
    public static FavoriteReadRes from(Favorite favorite) {
        return FavoriteReadRes.builder()
            .userId(favorite.getUserId())
            .storeId(favorite.getStoreId())
            .build();
    }
}
