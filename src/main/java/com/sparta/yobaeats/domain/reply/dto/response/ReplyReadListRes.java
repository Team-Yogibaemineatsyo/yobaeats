package com.sparta.yobaeats.domain.reply.dto.response;

import java.util.List;

public record ReplyReadListRes(
        List<ReplyReadRes> replies
) {
}
