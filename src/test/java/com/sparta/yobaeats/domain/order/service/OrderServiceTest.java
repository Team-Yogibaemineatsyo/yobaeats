package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.entity.OrderStatus;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    private UserService userService;

    private User user;
    private Store store;
    private Menu menu;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .nickName("testUser")
                .role(UserRole.ROLE_USER)
                .build();

        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .minOrderPrice(10000)
                .starRate(4.5)
                .isDeleted(false)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(22, 0))
                .user(user)
                .build();

        menu = new Menu(1L, store, "Test Menu", 15000, "Delicious food", false);
    }

    @Test
    void 주문_생성_성공() {
        // given
        OrderCreateReq orderCreateReq = new OrderCreateReq(store.getId(), menu.getId());

        when(storeService.findStoreById(store.getId())).thenReturn(store);
        when(menuService.findMenuById(menu.getId())).thenReturn(menu);
        when(userService.findUserById(user.getId())).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order(1L, user, store, menu, OrderStatus.PENDING));

        // when
        Long orderId = orderService.createOrder(orderCreateReq, user.getId());

        // then
        assertNotNull(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void 주문_조회_성공() {
        // given
        Order order = new Order(1L, user, store, menu, OrderStatus.PENDING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when
        Order foundOrder = orderService.findOrderById(order.getId());

        // then
        assertEquals(order.getId(), foundOrder.getId());
        assertEquals(user.getId(), foundOrder.getUser().getId());
    }

    @Test
    void 주문_조회_실패() {
        // given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
            orderService.findOrderById(orderId);
        });
        assertEquals(ErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void 주문_상태_변경_성공() {
        // given
        User owner = User.builder()
                .id(2L)
                .email("owner@example.com")
                .password("password")
                .nickName("ownerUser")
                .role(UserRole.ROLE_OWNER)
                .build();

        Store ownerStore = Store.builder()
                .id(2L)
                .name("Owner's Store")
                .user(owner)
                .minOrderPrice(15000)
                .build();

        Order order = new Order(1L, user, ownerStore, menu, OrderStatus.PENDING);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(userService).validateUser(ownerStore.getUser().getId(), owner.getId());


        // when
        orderService.updateOrderStatus(order.getId(), owner.getId());

        // then
        assertEquals(OrderStatus.ORDER_REQUESTED, order.getOrderStatus());
        verify(orderRepository).findById(order.getId());
    }

    @Test
    void 주문_생성_실패_스토어_없음() {
        // given
        when(storeService.findStoreById(store.getId())).thenThrow(new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND));
        OrderCreateReq orderCreateReq = new OrderCreateReq(store.getId(), menu.getId());

        // when & then
        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
            orderService.createOrder(orderCreateReq, user.getId());
        });
        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
    }
}
