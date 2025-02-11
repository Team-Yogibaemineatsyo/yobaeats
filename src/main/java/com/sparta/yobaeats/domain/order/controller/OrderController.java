package com.sparta.yobaeats.domain.order.controller;

import com.sparta.yobaeats.domain.order.dto.response.OrderReadDetailRes;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.service.OrderService;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
     * 주문을 생성하는 API
     *
     * @param userDetails 인증된 사용자 정보
     * @return 생성된 주문의 URI와 함께 201 Created 응답
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long orderId = orderService.createOrder(userDetails.getId());
        URI uri = UriBuilderUtil.create("/api/orders/{orderId}", orderId);

        return ResponseEntity.created(uri).build(); // 201 Created 응답
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderReadDetailRes> readOrder(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(orderService.readOrderInfo(orderId, userDetails.getId())); // 200 OK 응답
    }

    /**
     * 주문 상태를 업데이트하는 API
     *
     * @param orderId     주문의 ID
     * @param userDetails 인증된 사용자 정보
     * @return 200 OK 응답
     */
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        orderService.updateOrderStatus(orderId, userDetails.getId());
        return ResponseEntity.ok().build(); // 200 OK 응답
    }
}
