package com.sparta.yobaeats.domain.order.controller;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.request.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.service.OrderService;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * @param orderCreateReq 주문 생성에 필요한 요청 데이터
     * @return 생성된 주문의 URI와 함께 201 Created 응답
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody @Valid OrderCreateReq orderCreateReq, // 쉼표로 구분
            @AuthenticationPrincipal UserDetailsCustom userDetails // 여기에 쉼표 추가
    ) {
        Long userId = userDetails.getId(); // 사용자 ID 가져오기
        Long orderId = orderService.createOrder(orderCreateReq, userDetails); // userId를 함께 전달
        URI uri = UriBuilderUtil.create("/api/orders/{orderId}", orderId);

        return ResponseEntity.created(uri).build(); // 201 Created 응답
    }

    /**
     * 주문 상태를 업데이트하는 API
     *
     * @param orderId 주문의 ID
     * @param orderUpdateReq 상태 업데이트에 필요한 요청 데이터
     * @return 200 OK 응답
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestBody @Valid OrderUpdateReq orderUpdateReq,
            @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        Long userId = userDetails.getId(); // 사용자 ID 가져오기
        orderService.updateOrderStatus(orderId, orderUpdateReq, userDetails); // 상태 업데이트 처리
        return ResponseEntity.ok().build(); // 200 OK 응답
    }
}
