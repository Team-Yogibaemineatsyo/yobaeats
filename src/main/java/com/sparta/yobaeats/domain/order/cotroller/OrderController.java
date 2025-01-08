package com.sparta.yobaeats.domain.order.controller;

import com.sparta.yobaeats.domain.order.dto.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderCreateReq orderCreateReq
    ) {
        Order order = orderService.createOrder(orderCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestBody OrderUpdateReq orderUpdateReq) {
        orderService.updateOrderStatus(orderId, orderUpdateReq);
        return ResponseEntity.ok().build();
    }

}
