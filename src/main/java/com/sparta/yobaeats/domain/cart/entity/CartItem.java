package com.sparta.yobaeats.domain.cart.entity;

import com.sparta.yobaeats.global.exception.InvalidException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem implements Serializable {

    private Long menuId;
    private Integer quantity;

    @Builder
    public CartItem(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void increaseQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(Integer quantity) {
        if (this.quantity - quantity < 0) {
            throw new InvalidException(ErrorCode.INVALID_DECREASE_QUANTITY);
        }

        this.quantity += quantity;
    }
}
