package com.sparta.yobaeats.domain.reply.dto.request;

import com.sparta.yobaeats.domain.reply.dto.ReplyValidationMessage;
import com.sparta.yobaeats.domain.reply.entity.Reply;
import com.sparta.yobaeats.domain.review.dto.ReviewValidationMessage;
import com.sparta.yobaeats.domain.review.entity.Review;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReplyCreateReq(
        @NotNull(message = ReplyValidationMessage.REVIEW_ID_BLANK_MESSAGE)
        Long reviewId,
        @NotBlank(message = ReplyValidationMessage.REPLY_BLANK_MESSAGE)
        @Size(max = ReplyValidationMessage.REPLY_MAX, message = ReplyValidationMessage.REPLY_MAX_MESSAGE)
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