package com.sparta.yobaeats.domain.menu.dto.request;

public record MenuUpdateReq(
        String menuName,
        Integer menuPrice,
        String description
) {
}
