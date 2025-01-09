package com.sparta.yobaeats.domain.review.dto.request;

import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.review.entity.Review;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewReq(

    @NotBlank(message = "주문 ID는 필수 입력 항목입니다.")
    Long orderId,

    @NotNull(message = "별점은 필수 입력 항목입니다.")
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하이어야 합니다.")
    int star,

    @NotBlank(message = "리뷰 내용은 필수 입력 항목입니다.")
    @Size(min = 1, max = 100, message = "리뷰 내용은 1자 이상 100자 이하로 입력해주세요.")
    String contents
) {

    public Review to(User user, Order order, Store store) {
        return Review.builder()
            .user(user)
            .order(order)
            .store(store)
            .contents(this.contents)
            .star(this.star)
            .build();
    }
}
