package com.sparta.yobaeats.domain.auth.service;

import com.sparta.yobaeats.domain.auth.dto.request.AuthLoginRequest;
import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signup(AuthSignupRequest authSignupRequest) {
        boolean isExists = userRepository.existsByEmail(authSignupRequest.email());

        if (isExists) {
            throw new ConflictException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
    }

    public void login(AuthLoginRequest authLoginRequest) {

    }

    public void logout() {

    }
}
