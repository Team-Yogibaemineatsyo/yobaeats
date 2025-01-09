package com.sparta.yobaeats.domain.review.dto.response;

import com.sparta.yobaeats.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ReviewRes(Long reviewId, int star, String contents) {

    public static ReviewRes from(Review review) {
        return ReviewRes.builder()
            .reviewId(review.getId())
            .star(review.getStar())
            .contents(review.getContents())
            .build();
    }
}
