package com.sparta.yobaeats.domain.review.dto.response;

import com.sparta.yobaeats.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ReviewReadInfoRes(
    Long reviewId,
    int star,
    String content
) {

    public static ReviewReadInfoRes from(Review review) {
        return ReviewReadInfoRes.builder()
            .reviewId(review.getId())
            .star(review.getStar())
            .content(review.getContent())
            .build();
    }
}
