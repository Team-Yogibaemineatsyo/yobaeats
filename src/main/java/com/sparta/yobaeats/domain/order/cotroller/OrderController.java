package com.sparta.yobaeats.domain.order.controller;

import com.sparta.yobaeats.domain.order.dto.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 관련 API 요청을 처리하는 Controller 클래스
 *
 * 이 클래스는 주문 생성 및 상태 업데이트와 같은 작업을 처리하며,
 * 클라이언트 요청을 Service 계층으로 전달합니다.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 API
     *
     * @param orderCreateReq 주문 생성 요청 데이터
     *                        - 주문에 포함될 메뉴, 수량, 사용자 정보 등
     * @return HTTP 201(CREATED) 상태 코드와 생성된 주문 데이터를 반환
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody OrderCreateReq orderCreateReq
    ) {
        Order order = orderService.createOrder(orderCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 주문 상태 업데이트 API
     *
     * @param orderId 상태를 업데이트할 주문 ID
     * @param orderUpdateReq 주문 상태 업데이트 요청 데이터
     *                        - 새로운 상태 값 (예: PREPARING, COMPLETED 등)
     * @return HTTP 200(OK) 상태 코드 반환
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderUpdateReq orderUpdateReq) {
        orderService.updateOrderStatus(orderId, orderUpdateReq); // 주문 상태 업데이트 처리
        return ResponseEntity.ok().build();
    }
}
