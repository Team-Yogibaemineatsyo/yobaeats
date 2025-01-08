package com.sparta.yobaeats.domain.store.dto.response;

import com.sparta.yobaeats.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalTime;

@Builder(access = AccessLevel.PRIVATE)
public record StoreReadDetailRes(
        Long storeId,
        String storeName,
        LocalTime openAt,
        LocalTime closeAt,
        Integer minOrderPrice,
        Double starRate
//        List<MenuDetailRes> menus
) {

    public static StoreReadDetailRes from(Store store) {
        return StoreReadDetailRes.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .openAt(store.getOpenAt())
                .closeAt(store.getCloseAt())
                .minOrderPrice(store.getMinOrderPrice())
                .starRate(store.getStarRate())
                .build();
    }
}
