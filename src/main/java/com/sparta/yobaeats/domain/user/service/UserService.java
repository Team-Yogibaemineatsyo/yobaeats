package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.UserUpdateReq;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateUser(Long userId, UserUpdateReq req) {
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if(!user.getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        user.updateUser(req);

        userRepository.save(user);
    }
}
