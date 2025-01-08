package com.sparta.yobaeats.domain.store.dto.response;

import com.sparta.yobaeats.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoreReadSimpleRes(
        Long storeId,
        String storeName,
        Integer minOrderPrice,
        Double starRate
) {

    public static StoreReadSimpleRes from(Store store) {
        return StoreReadSimpleRes.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .minOrderPrice(store.getMinOrderPrice())
                .starRate(store.getStarRate())
                .build();
    }
}
