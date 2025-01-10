package com.sparta.yobaeats.domain.favorite.dto.response;

import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import lombok.Builder;

@Builder
public record FavoriteReadRes(
    Long id,
    Long userId,
    Long storeId
) {

    public static FavoriteReadRes from(Favorite favorite) {
        return FavoriteReadRes.builder()
            .id(favorite.getId())
            .userId(favorite.getUserId())
            .storeId(favorite.getStoreId())
            .build();
    }
}
