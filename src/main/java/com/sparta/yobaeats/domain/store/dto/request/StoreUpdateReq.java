package com.sparta.yobaeats.domain.store.dto.request;

import com.sparta.yobaeats.global.annotation.AtLeastOneNotNull;

import java.time.LocalTime;

@AtLeastOneNotNull(message = "요청에 수정할 내용이 없습니다")
public record StoreUpdateReq(
        String storeName,
        LocalTime openAt,
        LocalTime closeAt,
        Integer minOrderPrice
) {
}
