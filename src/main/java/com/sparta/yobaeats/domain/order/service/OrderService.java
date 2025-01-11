package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
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
    private final UserService userService;

    /**
     * 주문 생성 메서드
     *
     * 주어진 주문 생성 요청 DTO를 사용하여 새로운 주문을 생성합니다.
     * 스토어와 메뉴 정보를 확인하고, 현재 인증된 사용자 정보를 기반으로 주문을 저장합니다.
     *
     * @param orderCreateReq 주문 생성 요청 DTO
     * @return 생성된 주문의 ID
     */
    public Long createOrder(OrderCreateReq orderCreateReq, Long userId) {
        // 스토어 조회
        Store store = storeService.findStoreById(orderCreateReq.storeId());

        // 메뉴 조회
        Menu menu = menuService.findMenuById(orderCreateReq.menuId());

        // 사용자 객체 조회
        User user = userService.findUserById(userId);

        // 주문 엔티티 생성
        Order order = orderCreateReq.toEntity(store, menu, user);

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 생성된 주문 ID 반환
        return savedOrder.getId();
    }

    /**
     * 주문 상태 업데이트 메서드
     *
     * 주어진 주문 ID와 주문 업데이트 요청 DTO를 사용하여 주문 상태를 업데이트합니다.
     * 주문의 현재 상태와 요청된 상태가 일치하는지 확인 후 상태를 변경합니다.
     *
     * @param orderId        주문 ID
     */
    public void updateOrderStatus(Long orderId, Long userId) {
        // 주문 조회
        Order order = findOrderById(orderId);

        // 스토어 객체 가져오기
        Store store = order.getStore(); // 스토어 객체를 가져옴

        // 소유자 체크
        userService.validateUser(store.getUser().getId(), userId);

        // 상태 변경
        order.changeStatusToNext();
    }

    /**
     * 주문 ID로 주문을 찾는 메서드입니다.
     *
     * @param orderId 찾고자 하는 주문의 ID
     * @return 주문 객체
     */
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND)); // 주문이 없을 경우 예외 처리
    }
}
