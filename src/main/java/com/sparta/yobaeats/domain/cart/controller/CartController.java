package com.sparta.yobaeats.domain.cart.controller;

import com.sparta.yobaeats.domain.cart.dto.request.CartCreateReq;
import com.sparta.yobaeats.domain.cart.dto.request.CartItemQuantityUpdateReq;
import com.sparta.yobaeats.domain.cart.dto.response.CartReadDetailRes;
import com.sparta.yobaeats.domain.cart.service.CartService;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> createCart(
            @RequestBody @Valid CartCreateReq request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.addItemToCart(request, userDetails.getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CartReadDetailRes> readCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.readCartInfo(userDetails.getId()));
    }

    @PatchMapping("/{menuId}/quantity")
    public ResponseEntity<Void> updateCart(
            @PathVariable Long menuId,
            @RequestBody @Valid CartItemQuantityUpdateReq request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.updateCartItemQuantity(menuId, request, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
