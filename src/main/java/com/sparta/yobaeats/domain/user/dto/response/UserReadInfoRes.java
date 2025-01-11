package com.sparta.yobaeats.domain.user.dto.response;

import com.sparta.yobaeats.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserReadInfoRes(
        Long userId,
        String email,
        String nickName,
        String userRole
) {

    public static UserReadInfoRes from(User user) {
        return UserReadInfoRes.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .userRole(user.getRole().toString())
                .build();
    }
}