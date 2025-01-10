package com.sparta.yobaeats.domain.auth.service;

import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Long signup(AuthSignupRequest authSignupRequest) {
        boolean isExists = userRepository.existsByEmail(authSignupRequest.email());

        if (isExists) {
            throw new ConflictException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(authSignupRequest.password());
        User user = authSignupRequest.toEntity(encodedPassword);
        User save = userRepository.save(user);

        return save.getId();
    }
}
