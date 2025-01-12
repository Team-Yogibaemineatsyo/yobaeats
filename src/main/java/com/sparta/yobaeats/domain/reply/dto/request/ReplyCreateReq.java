package com.sparta.yobaeats.domain.reply.dto.request;

import com.sparta.yobaeats.domain.reply.entity.Reply;
import com.sparta.yobaeats.domain.review.entity.Review;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReplyCreateReq(
        @NotNull
        Long reviewId,
        @NotBlank
        String content
) {

    public Reply to(User user, Review review) {
        return Reply.builder()
                .user(user)
                .review(review)
                .content(content)
                .build();
    }
}