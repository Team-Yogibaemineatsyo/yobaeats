package com.sparta.yobaeats.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.yobaeats.domain.menu.dto.response.MenuDetailRes;
import com.sparta.yobaeats.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StoreReadDetailRes(
        Long storeId,
        String storeName,
        @JsonFormat(pattern = "HH:mm")
        LocalTime openAt,
        @JsonFormat(pattern = "HH:mm")
        LocalTime closeAt,
        Integer minOrderPrice,
        Double starRate,
        List<MenuDetailRes> menus
) {

    public static StoreReadDetailRes from(Store store) {
        return StoreReadDetailRes.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .openAt(store.getOpenAt())
                .closeAt(store.getCloseAt())
                .minOrderPrice(store.getMinOrderPrice())
                .starRate(store.getStarRate())
                .menus(store.getMenus()
                        .stream()
                        .map(MenuDetailRes::from)
                        .toList())
                .build();
    }
}
