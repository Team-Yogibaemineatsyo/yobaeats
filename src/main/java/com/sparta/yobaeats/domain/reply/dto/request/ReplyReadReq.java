package com.sparta.yobaeats.domain.reply.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReplyReadReq(
        @NotBlank
        String content
) {
}