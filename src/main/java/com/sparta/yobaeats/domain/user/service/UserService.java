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
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserRes findById(Long userId) {
        User user = findUserById(userId);
        user.isDeletedUser();

        // 뒤의 userId는 추후에 토큰아이디로 변경
        validateUser(user.getId(), userId);

        return user.from();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateReq req) {
        User user = findUserById(userId);
        user.isDeletedUser();

        // 뒤의 userId는 추후에 토큰아이디로 변경
        validateUser(user.getId(), userId);

        user.updateUser(req);
    }

    @Transactional
    public void deleteUser(UserDeleteReq req, Long userId) {
        User user = findUserById(userId);
        user.isDeletedUser();

        // 뒤의 userId는 추후에 토큰아이디로 변경
        validateUser(user.getId(), userId);

        //if (!passwordEncoder.matches(req.password, user.getPassword())) {
        //    throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);

        user.softDelete();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateUser(Long userId, Long tokenUserId) {
        if (!userId.equals(tokenUserId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    /*
    *
    private void validatePassword(String rawPassword, String password) {
        if (!passwordEncoder.matches(rawPassword,password)) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }
    }
     */
}
