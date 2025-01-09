package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.response.UserReadInfoRes;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateInfoReq;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserReadInfoRes findById(Long userId) {
        return UserReadInfoRes.from(findUserById(userId));
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateInfoReq req) {
        String newPassword = (req.password() != null) ? passwordEncoder.encode(req.password()) : null;
        findUserById(userId).update(newPassword, req.nickName());
    }

    @Transactional
    public void deleteUser(UserDeleteReq req, Long userId) {
        User user = findUserById(userId);
        validatePassword(req.password(), user.getPassword());

        user.softDelete();
    }

    public User findUserById(Long userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateUser(Long userId, Long tokenUserId) {
        if (!userId.equals(tokenUserId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    private void validatePassword(String rawPassword, String password) {
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
