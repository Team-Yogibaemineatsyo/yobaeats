package com.sparta.yobaeats.domain.auth.service;

import com.sparta.yobaeats.domain.auth.dto.request.AuthLoginRequest;
import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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

    public String login(AuthLoginRequest authLoginRequest) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authLoginRequest.email(), authLoginRequest.password());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);

        return jwtUtil.generateTokenByAuthentication(authentication);
    }
}
