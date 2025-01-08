package com.sparta.yobaeats.domain.auth.service;

import com.sparta.yobaeats.domain.auth.dto.request.AuthLoginRequest;
import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(AuthSignupRequest authSignupRequest) {
        boolean isExists = userRepository.existsByEmail(authSignupRequest.email());

        if (isExists) {
            throw new ConflictException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(authSignupRequest.password());
        User user = authSignupRequest.toEntity(encodedPassword);

        userRepository.save(user);
    }

    public String login(AuthLoginRequest authLoginRequest) {
        User user = userRepository.findByEmail(authLoginRequest.email())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(authLoginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtUtil.generateToken(user.getId(), user.getRole());
    }

    public void logout() {

    }
}
