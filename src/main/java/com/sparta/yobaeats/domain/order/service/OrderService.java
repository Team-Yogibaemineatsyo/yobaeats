package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.request.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;  // Import StoreService
import com.sparta.yobaeats.domain.menu.service.MenuService;  // Import MenuService
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreService storeService;
    private final MenuService menuService;

    /**
     * 주문 생성 메서드
     */
    public Long createOrder(OrderCreateReq orderCreateReq) {
        // 가게 조회: StoreService에서 예외처리된 메서드 사용
        Store store = storeService.findStoreById(orderCreateReq.storeId());

        // 메뉴 조회: MenuService에서 예외처리된 메서드 사용
        Menu menu = menuService.findMenuById(orderCreateReq.menuId());

        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();  // 현재 로그인된 사용자

        // 주문 엔티티 생성
        Order order = orderRepository.save(orderCreateReq.toEntity(store, menu, user));

        // 생성된 주문의 ID 반환
        return order.getId();  // 'Order' 객체의 ID 반환
    }

    /**
     * 주문 상태 업데이트 메서드
     */
    public void updateOrderStatus(Long orderId, OrderUpdateReq orderUpdateReq) {
        // 주문 조회
        Order order = findOrderById(orderId);

        // 상태 변경
        order.changeStatusToNext();  // nextStatus()를 호출하여 상태를 변경
    }

    /**
     * 주문 ID로 주문을 조회하는 메서드
     */
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }
}
