package com.sparta.yobaeats.domain.reply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.yobaeats.domain.reply.entity.Reply;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyReadRes(
        Long replyId,
        Long storeId,
        String storeName,
        UserRole userRole,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ReplyReadRes from(Reply reply) {
        if (reply == null || reply.getReview() == null) {
            return empty();
        }
        return ReplyReadRes.builder()
                .replyId(reply.getId())
                .storeId(reply.getReview().getStore().getId())
                .storeName(reply.getReview().getStore().getName())
                .userRole(reply.getUser().getRole())
                .comment(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }

    public static ReplyReadRes empty() {
        return ReplyReadRes.builder().build();
    }
}