package com.sparta.yobaeats.domain.review.dto.response;

import java.util.List;

public record ReviewReadInfoListRes(
    List<ReviewReadInfoRes> reviews
) {
}
