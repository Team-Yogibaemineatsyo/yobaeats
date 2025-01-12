package com.sparta.yobaeats.domain.review.dto.response;

import com.sparta.yobaeats.domain.reply.dto.response.ReplyReadRes;
import com.sparta.yobaeats.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record ReviewReadInfoRes(
    Long reviewId,
    Long userId,
    String nickName,
    int star,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    ReplyReadRes reply
) {

    public static ReviewReadInfoRes from(Review review) {
        return ReviewReadInfoRes.builder()
            .reviewId(review.getId())
            .userId(review.getUser().getId())
            .nickName(review.getUser().getNickName())
            .star(review.getStar())
            .content(review.getContent())
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .reply(com.sparta.yobaeats.domain.reply.dto.response.ReplyReadRes.from(review.getReply()))
            .build();
    }
}
