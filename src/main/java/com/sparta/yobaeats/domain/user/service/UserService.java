package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.UserRes;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserRes findUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        user.isDeletedUser();

        // 뒤의 userId는 추후에 토큰아이디로 변경
        if (!user.getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return user.from();
    }
}
