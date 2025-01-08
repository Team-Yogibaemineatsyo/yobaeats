package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.UserDeleteReq;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public void deleteUser(UserDeleteReq req, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        user.isDeletedUser();

        if(user.getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        //if (!passwordEncoder.matches(req.password, user.getPassword())) {
        //    throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        userRepository.delete(user);
    }
}
