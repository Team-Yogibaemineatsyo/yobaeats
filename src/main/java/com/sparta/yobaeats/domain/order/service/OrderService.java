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
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
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
        // 스토어 조회
        Store store = storeService.findStoreById(orderCreateReq.storeId());

        // 스토어가 존재하지 않는 경우 예외 처리
        if (store == null) {
            throw new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND);
        }

        Menu menu = menuService.findMenuById(orderCreateReq.menuId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE); // 인증되지 않은 사용자 처리
        }

        User user = (User) authentication.getPrincipal();  // 현재 로그인된 사용자

        // 주문 엔티티 생성
        Order order = orderRepository.save(orderCreateReq.toEntity(store, menu, user));

        // 주문이 성공적으로 생성되면 주문 ID를 반환
        return order.getId();
    }

    /**
     * 주문 상태 업데이트 메서드
     *
     * @param orderId 주문 ID
     * @param orderUpdateReq 주문 상태 업데이트 요청 DTO
     */
    public void updateOrderStatus(Long orderId, OrderUpdateReq orderUpdateReq) {
        // 주문 조회
        Order order = findOrderById(orderId);

        // 소유자 체크
        checkIfOwner(order.getStore().getId(), order.getStore().getUser().getId());

        // 요청된 상태가 현재 상태의 다음 상태인지 확인
        if (!order.getOrderStatus().nextStatus().equals(orderUpdateReq.orderStatus())) {
            throw new CustomRuntimeException(ErrorCode.ORDER_NOT_CHANGE);
        }

        // 상태 변경
        order.changeStatusToNext(); // 다음 상태로 변경
    }

    /**
     * 스토어의 소유자인지 확인하는 메서드
     *
     * @param storeId 스토어 ID
     * @param userId  사용자 ID
     * @throws CustomRuntimeException 소유자가 아닐 경우
     */
    public void checkIfOwner(Long storeId, Long userId) {
        Store store = storeService.findStoreById(storeId);
        if (store == null) {
            throw new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND);
        }
        if (!store.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE);
        }
    }

    /**
     * 주문 ID로 주문을 조회하는 메서드
     */
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }
}
