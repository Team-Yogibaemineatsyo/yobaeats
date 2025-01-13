package com.sparta.yobaeats.domain.order.service;

import com.sparta.yobaeats.domain.cart.entity.Cart;
import com.sparta.yobaeats.domain.cart.entity.CartItem;
import com.sparta.yobaeats.domain.cart.service.CartService;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.entity.OrderMenu;
import com.sparta.yobaeats.domain.order.entity.OrderStatus;
import com.sparta.yobaeats.domain.order.repository.OrderRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.List;
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

    @Mock
    private CartService cartService;

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
        Menu menu1 = Menu.builder().id(1L).menuName("Burger").menuPrice(5000).build();
        Menu menu2 = Menu.builder().id(2L).menuName("Fries").menuPrice(2000).build();

        List<CartItem> cartItems = List.of(
                CartItem.builder().menuId(menu1.getId()).quantity(2).build(),
                CartItem.builder().menuId(menu2.getId()).quantity(3).build()
        );

        Cart cart = Cart.builder().userId(user.getId()).storeId(store.getId()).items(cartItems).build();
        List<OrderMenu> menus = List.of(OrderMenu.builder().build());

        when(userService.findUserById(user.getId())).thenReturn(user);
        when(cartService.findCartByUserId(user.getId())).thenReturn(cart);
        when(menuService.findMenuById(1L)).thenReturn(menu1);
        when(menuService.findMenuById(2L)).thenReturn(menu2);
        when(storeService.findStoreById(store.getId())).thenReturn(store);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(Order.builder().id(1L).totalPrice(30000).user(user).store(store).menus(menus).build());

        // when
        Long orderId = orderService.createOrder(user.getId());

        // then
        assertNotNull(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void 주문_조회_성공() {
        // given
        Order order = Order.builder().id(1L).user(user).store(store).build();
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

        Order order = Order.builder().id(1L).user(user).store(ownerStore).orderStatus(OrderStatus.ORDER_REQUESTED).build();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(userService).validateUser(ownerStore.getUser().getId(), owner.getId());


        // when
        orderService.updateOrderStatus(order.getId(), owner.getId());

        // then
        assertEquals(OrderStatus.ORDER_ACCEPTED, order.getOrderStatus());
        verify(orderRepository).findById(order.getId());
    }

    @Test
    void 주문_생성_실패_장바구니_없음() {
        // given
        when(cartService.findCartByUserId(user.getId())).thenThrow(new NotFoundException(ErrorCode.CART_NOT_FOUND));

        // when & then
        CustomRuntimeException exception = assertThrows(NotFoundException.class, () -> {
            orderService.createOrder(user.getId());
        });
        assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
    }
}
