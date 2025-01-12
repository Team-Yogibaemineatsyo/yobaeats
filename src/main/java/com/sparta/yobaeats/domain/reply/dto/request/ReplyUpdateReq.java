package com.sparta.yobaeats.domain.reply.dto.request;

import com.sparta.yobaeats.domain.reply.dto.ReplyValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyUpdateReq(
        @NotBlank(message = ReplyValidationMessage.REPLY_BLANK_MESSAGE)
        @Size(max = ReplyValidationMessage.REPLY_MAX, message = ReplyValidationMessage.REPLY_MAX_MESSAGE)
        String content
) {
}