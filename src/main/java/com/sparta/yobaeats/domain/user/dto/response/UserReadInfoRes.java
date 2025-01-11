package com.sparta.yobaeats.domain.user.dto.response;

import com.sparta.yobaeats.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserReadInfoRes(
        String email,
        String nickName
) {

    public static UserReadInfoRes from(User user) {
        return UserReadInfoRes.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }
}