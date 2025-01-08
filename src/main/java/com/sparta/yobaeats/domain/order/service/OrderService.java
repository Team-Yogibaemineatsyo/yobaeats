package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.domain.order.dto.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
// import com.sparta.yobaeats.domain.store.repository.StoreRepository;
// import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    // private final StoreRepository storeRepository;
    // private final UserRepository userRepository;

    @Transactional
    public Order createOrder(OrderCreateReq orderCreateReq) { // userId를 파라미터로 추가
        Long storeId = orderCreateReq.storeId(); // 주문할 가게 ID
        Long menuId = orderCreateReq.menuId(); // 주문할 메뉴 ID

        // 메뉴를 조회하여 존재하는지 확인
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));

        // 주문 생성
        Order order = Order.builder()
                .storeId(storeId) // 가게 ID 설정
                .menu(menu) // 메뉴 객체 설정
                .status(Order.Status.ORDER_REQUESTED)  // 초기 상태를 enum으로 설정
                .build();

        return orderRepository.save(order);
    }

//    @Transactional
//    public Order createOrder(OrderCreateReq orderCreateReq) {
//        Long storeId = orderCreateReq.storeId(); // 주문할 가게 ID
//        Long menuId = orderCreateReq.menuId(); // 주문할 메뉴 ID
//
//        // 가게를 조회하여 존재하는지 확인
//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND));
//
//        // 메뉴를 조회하여 존재하는지 확인
//        Menu menu = menuRepository.findById(menuId)
//                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));
//
//        Order order = Order.builder()
//                .user(user) // User 객체 설정
//                .store(store) // Store 객체 설정
//                .menu(menu) // Menu 객체 설정
//                .status("ORDERED") // 초기 상태 설정
//                .build();
//
//        return orderRepository.save(order);
//    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderUpdateReq orderUpdateReq) {
        // 주문을 조회합니다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND));

        // OrderUpdateReq에서 새로운 상태를 가져옵니다.
        Order.Status newStatus = orderUpdateReq.status(); // Order.Status 타입으로 가져옵니다.

        // 주문 상태 변경
        order.changeStatus(newStatus);
    }

}