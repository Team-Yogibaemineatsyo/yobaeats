package com.sparta.yobaeats.domain.cart.service;

import com.sparta.yobaeats.domain.cart.dto.request.CartCreateReq;
import com.sparta.yobaeats.domain.cart.entity.Cart;
import com.sparta.yobaeats.domain.cart.entity.CartItem;
import com.sparta.yobaeats.domain.cart.repository.CartRedisRepository;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MenuService menuService;
    private final CartRedisRepository cartRepository;

    @Transactional
    public void addItemToCart(CartCreateReq request, Long userId) {
        Cart cart = findCartByUserId(userId);

        menuService.findMenuById(request.menuId());

        List<CartItem> cartItems = cart.getItems();
        cartItems.stream()
                .filter(cartItem -> cartItem.getMenuId().equals(request.menuId()))
                .findFirst()
                .ifPresentOrElse(item -> item.increaseQuantity(request.quantity()),
                        () -> cartItems.add(request.toEntity()));

        cartRepository.save(cart);
    }

    private Cart findCartByUserId(Long userId) {
        return cartRepository.findById(userId)
                .orElseGet(() -> createNewCart(userId));
    }

    private Cart createNewCart(Long userId) {
        return Cart.builder()
                .userId(userId)
                .build();
    }
}
