package com.sparta.yobaeats.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResponse {

    private String bearerToken;
}
