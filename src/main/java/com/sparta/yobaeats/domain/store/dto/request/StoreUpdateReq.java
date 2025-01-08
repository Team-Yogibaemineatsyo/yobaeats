package com.sparta.yobaeats.domain.store.dto.request;

import java.time.LocalTime;

public record StoreUpdateReq(
        String storeName,
        LocalTime openAt,
        LocalTime closeAt,
        Integer minOrderPrice
) {
}
