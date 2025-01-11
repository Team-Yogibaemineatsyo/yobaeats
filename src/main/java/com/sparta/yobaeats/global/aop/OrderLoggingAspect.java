package com.sparta.yobaeats.global.aop;

import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderLoggingAspect {

    private final OrderRepository orderRepository;

    /**
     * 주문 상태 업데이트 후에 호출되는 AOP 메서드.
     * 주문 상태가 변경된 시간을 기록하고, 변경된 주문 정보와 상태를 로그로 남깁니다.
     *
     * @param joinPoint AOP의 조인 포인트로, 현재 메서드 호출 정보를 담고 있습니다.
     */
    @AfterReturning(pointcut = "execution(* com.sparta.yobaeats.domain.order.service.OrderService.updateOrderStatus(..))")
    public void logOrderAction(JoinPoint joinPoint) {
        // 현재 시간을 기록합니다.
        LocalDateTime currentTime = LocalDateTime.now();

        // 원하는 형식으로 날짜와 시간을 포맷합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        // 조인 포인트에서 인수들을 가져옵니다.
        Object[] args = joinPoint.getArgs();
        Long orderId = (Long) args[0]; // 첫 번째 인수는 주문 ID

        // 주문 ID로 주문을 조회합니다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND)); // 주문이 없으면 예외 처리

        // 현재 주문 상태로부터 다음 상태를 자동으로 결정합니다.
        order.changeStatusToNext();

        // 해당 주문의 가게 ID를 가져옵니다.
        Long storeId = order.getStore().getId();

        // 로그에 주문 상태 업데이트 정보를 기록합니다.
        log.info("Order status updated: time={}, storeId={}, orderId={}, newStatus={}",
                formattedTime, storeId, orderId, order.getOrderStatus());
    }
}
