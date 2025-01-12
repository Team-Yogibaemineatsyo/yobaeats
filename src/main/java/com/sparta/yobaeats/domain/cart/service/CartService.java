package com.sparta.yobaeats.domain.cart.service;

import com.sparta.yobaeats.domain.cart.dto.request.CartCreateReq;
import com.sparta.yobaeats.domain.cart.dto.request.CartItemQuantityUpdateReq;
import com.sparta.yobaeats.domain.cart.dto.response.CartItemInfoRes;
import com.sparta.yobaeats.domain.cart.dto.response.CartReadDetailRes;
import com.sparta.yobaeats.domain.cart.entity.Cart;
import com.sparta.yobaeats.domain.cart.entity.CartItem;
import com.sparta.yobaeats.domain.cart.repository.CartRedisRepository;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.domain.store.entity.QuantityType;
import com.sparta.yobaeats.global.exception.InvalidException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final MenuService menuService;
    private final CartRedisRepository cartRepository;

    public void addItemToCart(CartCreateReq request, Long userId) {
        Cart cart = findAddCartByUserId(userId);

        menuService.findMenuById(request.menuId());

        List<CartItem> cartItems = cart.getItems();
        cartItems.stream()
                .filter(cartItem -> cartItem.getMenuId().equals(request.menuId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.changeQuantity(request.quantity()),
                        () -> cartItems.add(request.toEntity())
                );

        cartRepository.save(cart);
    }

    public CartReadDetailRes readCartInfo(Long userId) {
        Cart cart = findCartByUserId(userId);

        List<CartItem> items = cart.getItems();
        List<CartItemInfoRes> cartItems = items.stream()
                .map(cartItem -> CartItemInfoRes.of(cartItem, menuService.findMenuById(cartItem.getMenuId())))
                .toList();

        return CartReadDetailRes.from(cartItems);
    }

    public void updateCartItemQuantity(Long menuId, CartItemQuantityUpdateReq request, Long userId) {
        Cart cart = findCartByUserId(userId);

        menuService.findMenuById(menuId);

        List<CartItem> cartItems = cart.getItems();
        cartItems.stream()
                .filter(cartItem -> cartItem.getMenuId().equals(menuId))
                .findFirst()
                .ifPresentOrElse(
                        cartItem -> {
                            QuantityType type = request.increase() ? QuantityType.INCREASE : QuantityType.DECREASE;
                            cartItem.changeQuantity(type.getValue());

                            if (cartItem.getQuantity() == QuantityType.REMOVE.getValue()) {
                                cartItems.remove(cartItem);
                            }
                        },
                        () -> {
                            throw new InvalidException(ErrorCode.CART_ITEM_NOT_FOUND);
                        }
                );

        if (cartItems.isEmpty()) {
            cartRepository.delete(cart);
        } else {
            cartRepository.save(cart);
        }
    }

    private Cart findCartByUserId(Long userId) {
        return cartRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_NOT_FOUND));
    }

    private Cart findAddCartByUserId(Long userId) {
        return cartRepository.findById(userId)
                .orElseGet(() -> createNewCart(userId));
    }

    private Cart createNewCart(Long userId) {
        return Cart.builder()
                .userId(userId)
                .build();
    }
}
