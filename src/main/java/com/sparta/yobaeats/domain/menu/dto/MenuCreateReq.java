package com.sparta.yobaeats.domain.menu.dto;

public record MenuCreateReq(Long storeId, String menuName, Integer menuPrice, String description) {
}