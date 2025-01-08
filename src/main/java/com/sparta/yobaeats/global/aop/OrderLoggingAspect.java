package com.sparta.yobaeats.global.aop;

import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.dto.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderLoggingAspect {

    private final OrderRepository orderRepository;

    @AfterReturning(pointcut = "execution(* com.sparta.yobaeats.domain.order.service.OrderService.updateOrderStatus(..))")
    public void logOrderAction(JoinPoint joinPoint) {
        // 현재 시간을 기록합니다.
        LocalDateTime currentTime = LocalDateTime.now();

        // 원하는 형식으로 날짜와 시간을 포맷합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        Object[] args = joinPoint.getArgs();
        Long orderId = (Long) args[0]; // 첫 번째 인수는 orderId
        OrderUpdateReq orderUpdateReq = (OrderUpdateReq) args[1]; // 두 번째 인수는 OrderUpdateReq

        Order.Status newStatus = orderUpdateReq.status();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND));

        Long storeId = order.getStore().getId();

        log.info("Order status updated: time={}, storeId={}, orderId={}, newStatus={}",
                formattedTime, storeId, orderId, newStatus);
    }
}
