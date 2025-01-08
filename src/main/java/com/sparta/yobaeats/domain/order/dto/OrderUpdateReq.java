package com.sparta.yobaeats.domain.order.dto;

import com.sparta.yobaeats.domain.order.entity.Order;

public record OrderUpdateReq(Order.Status status) {
}
