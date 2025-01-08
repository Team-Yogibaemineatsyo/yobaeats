package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.UserRes;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserRes findUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        user.isDeletedUser();

        //나중에 토큰아이디 뒤의 userId엔 나중에 토큰에서 추출한 아이디 넣을거야
        if(!user.getId().equals(userId))  {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return user.from();
    }
}
