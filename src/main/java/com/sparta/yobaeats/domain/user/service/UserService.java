package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.response.UserRes;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateReq;
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
    //private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserRes findById(Long userId) {
        User user = findUserById(userId);
        user.isDeletedUser();

        // 뒤의 userId는 추후에 토큰아이디로 변경
        if (!user.getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return user.from();
    }

    public void updateUser(Long userId, UserUpdateReq req) {
        User user = findUserById(userId);
        user.isDeletedUser();

        // 뒤의 userId 나중에 토큰 Id로 변경
        if (!user.getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        user.updateUser(req);

        userRepository.save(user);
    }

    public void deleteUser(UserDeleteReq req, Long userId) {
        User user = findUserById(userId);
        user.isDeletedUser();

        // 뒤의 userId 나중에 토큰 Id로 변경
        if(user.getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        //if (!passwordEncoder.matches(req.password, user.getPassword())) {
        //    throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);

        user.softDelete();

        userRepository.save(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
