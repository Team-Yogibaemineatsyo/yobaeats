package com.sparta.yobaeats.domain.review.dto.response;

import com.sparta.yobaeats.domain.review.entity.Review;

public record ReviewRes(Long reviewId, int star, String comment) {

    public static ReviewRes from(Review review) {
        return new ReviewRes(
            review.getId(),
            review.getStar(),
            review.getContents()
        );
    }
}
