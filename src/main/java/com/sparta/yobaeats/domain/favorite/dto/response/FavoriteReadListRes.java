package com.sparta.yobaeats.domain.favorite.dto.response;

import java.util.List;

public record FavoriteReadListRes(
        List<FavoriteReadRes> favorites
) {
}