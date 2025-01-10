package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.dto.request.OrderUpdateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.entity.OrderStatus; // Ensure you import OrderStatus
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private MenuService menuService;

    // 주문 생성 성공 테스트
//    @Test
//    @WithMockUser(roles = "USER")
//    void 주문_생성_성공() {
//        Long storeId = 1L;
//        Long menuId = 1L;
//        User user = User.builder() // 사용자로 설정
//                .id(1L)
//                .email("user@example.com")
//                .password("password")
//                .nickName("username")
//                .role(UserRole.ROLE_USER) // USER 역할 설정
//                .build();
//
//        Store store = Store.builder()
//                .id(storeId)
//                .name("가게 이름")
//                .minOrderPrice(5000)
//                .starRate(0.0)
//                .isDeleted(false)
//                .openAt(LocalTime.of(9, 0))
//                .closeAt(LocalTime.of(21, 0))
//                .user(user) // 사용자 연결
//                .build();
//
//        Menu menu = new Menu(menuId, store, "메뉴 이름", 10000, "메뉴 설명", false);
//
//        // 요청 DTO 설정
//        OrderCreateReq orderCreateReq = new OrderCreateReq(storeId, menuId);
//
//        // Mock 설정
//        when(storeService.findStoreById(storeId)).thenReturn(store);
//        when(menuService.findMenuById(menuId)).thenReturn(menu);
//
//        // OrderStatus 추가
//        Order order = new Order(1L, user, store, menu, OrderStatus.PENDING); // 사용자로 설정
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        // when
//        Long orderId = orderService.createOrder(orderCreateReq);
//
//        // then
//        assertNotNull(orderId);
//        verify(orderRepository).save(any(Order.class));
//    }

    // 주문 조회 성공 테스트
    @Test
    void 주문_조회_성공() {
        Long orderId = 1L;
        Long storeId = 1L;
        User owner = User.builder()
                .id(1L)
                .email("owner@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_OWNER)
                .build();

        Store store = Store.builder()
                .id(storeId)
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0)
                .isDeleted(false)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(21, 0))
                .user(owner)
                .build();

        Menu menu = new Menu(1L, store, "메뉴 이름", 10000, "메뉴 설명", false);
        // OrderStatus 추가
        Order order = new Order(orderId, owner, store, menu, OrderStatus.PENDING);

        // Mock 설정
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        Order foundOrder = orderService.findOrderById(orderId);

        // then
        assertEquals(orderId, foundOrder.getId());
        assertEquals(owner.getId(), foundOrder.getUser().getId());
    }

    // 주문 조회 실패 테스트
    @Test

    void 주문_조회_실패() {
        Long orderId = 1L;

        // Mock 설정
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
            orderService.findOrderById(orderId);
        });
        assertEquals(ErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
    }

    // 주문 상태 변경 성공 테스트
    @Test
    @WithMockUser(roles = "OWNER")
    void 주문_상태_변경_성공() {
        // 준비 단계: 필요한 객체 생성
        Long orderId = 1L;
        Long storeId = 1L; // 스토어 ID 추가
        User owner = User.builder()
                .id(1L)
                .email("owner@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_OWNER)
                .build();

        // 스토어 객체 생성
        Store store = Store.builder()
                .id(storeId) // 스토어 ID 연결
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0)
                .isDeleted(false)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(21, 0))
                .user(owner) // 가게 소유자 연결
                .build();

        // 메뉴 객체 생성
        Menu menu = new Menu(1L, store, "메뉴 이름", 10000, "메뉴 설명", false);

        // 초기 상태를 PENDING으로 설정
        Order order = new Order(orderId, owner, store, menu, OrderStatus.PENDING);

        // Mock 설정: 주문과 스토어가 데이터베이스에서 조회될 때 해당 객체를 반환하도록 설정
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(storeService.findStoreById(storeId)).thenReturn(store); // 스토어 Mock 설정

        // 주문 상태 변경 메서드 호출
        OrderUpdateReq orderUpdateReq = new OrderUpdateReq(OrderStatus.ORDER_REQUESTED);
        orderService.updateOrderStatus(orderId, orderUpdateReq);

        // then: 상태가 ORDER_REQUESTED로 올바르게 변경되었는지 확인
        assertEquals(OrderStatus.ORDER_REQUESTED, order.getOrderStatus()); // 다음 상태로 바뀌어야 함

        // verify: 주문이 DB에서 조회되었는지 확인
        verify(orderRepository).findById(orderId);
        verify(storeService).findStoreById(storeId); // 스토어 조회도 검증
    }
}
