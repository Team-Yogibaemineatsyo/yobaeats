package com.sparta.yobaeats.domain.reply.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReplyUpdateReq(
        @NotBlank
        String content
) {
}