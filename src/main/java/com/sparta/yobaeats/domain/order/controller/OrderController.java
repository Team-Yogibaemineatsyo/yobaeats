package com.sparta.yobaeats.domain.order.controller;

import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.request.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.service.OrderService;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 주문 관련 API 요청을 처리하는 Controller 클래스
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 API
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody @Valid OrderCreateReq orderCreateReq
    ) {
        Long orderId = orderService.createOrder(orderCreateReq);
        URI uri = UriBuilderUtil.create("/api/orders/{orderId}", orderId);

        return ResponseEntity.created(uri).build();
    }

    /**
     * 주문 상태 업데이트 API
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestBody @Valid OrderUpdateReq orderUpdateReq
    ) {
        orderService.updateOrderStatus(orderId, orderUpdateReq); // 상태 업데이트 처리
        return ResponseEntity.ok().build();
    }
}
