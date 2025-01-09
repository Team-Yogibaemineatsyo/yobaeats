package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.domain.order.dto.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 관리 로직을 처리하는 Service 클래스
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * 주문 생성 메서드
     *
     * @param orderCreateReq 주문 생성 요청 데이터
     * @return 생성된 Order 객체
     * @throws CustomRuntimeException STORE_NOT_FOUND: 가게를 찾을 수 없는 경우
     * @throws CustomRuntimeException MENU_NOT_FOUND: 메뉴를 찾을 수 없는 경우
     */
    @Transactional
    public Order createOrder(OrderCreateReq orderCreateReq) {
        // 가게 조회
        Store store = storeRepository.findById(orderCreateReq.storeId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND));

        // 메뉴 조회
        Menu menu = menuRepository.findById(orderCreateReq.menuId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));

        // DTO 팩토리 메서드를 사용해 Order 엔티티 생성
        Order order = orderCreateReq.toEntity(store, menu);

        // 주문 저장
        return orderRepository.save(order);
    }

    /**
     * 주문 상태 업데이트 메서드
     *
     * @param orderId 업데이트할 주문의 ID
     * @param orderUpdateReq 주문 상태 수정 요청 데이터
     * @throws CustomRuntimeException ORDER_NOT_FOUND: 주문을 찾을 수 없는 경우
     */
    @Transactional
    public void updateOrderStatus(Long orderId, OrderUpdateReq orderUpdateReq) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND));

        // 새로운 상태로 변경
        order.changeStatus(orderUpdateReq.status());
    }

    /**
     * 주문 ID로 주문을 조회하는 메서드.
     *
     * @param orderId 조회할 주문의 ID
     * @return Order 객체: 조회된 주문 정보
     * @throws NotFoundException ORDER_NOT_FOUND: 주문을 찾을 수 없는 경우 발생
     */
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }
}
