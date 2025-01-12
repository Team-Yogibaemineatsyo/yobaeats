package com.sparta.yobaeats.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.yobaeats.domain.menu.dto.response.MenuOrderInfoRes;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.entity.OrderMenu;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record OrderReadDetailRes(
        Long orderId,
        List<MenuOrderInfoRes> menus,
        Integer totalPrice,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime orderCreateTime
) {

    public static OrderReadDetailRes from(Order order) {
        return OrderReadDetailRes.builder()
                .orderId(order.getId())
                .menus(getMenuOrderInfo(order.getMenus()))
                .totalPrice(order.getTotalPrice())
                .orderCreateTime(order.getCreatedAt())
                .build();
    }

    private static List<MenuOrderInfoRes> getMenuOrderInfo(List<OrderMenu> menus) {
        return menus.stream()
                .map(MenuOrderInfoRes::from)
                .toList();
    }
}
