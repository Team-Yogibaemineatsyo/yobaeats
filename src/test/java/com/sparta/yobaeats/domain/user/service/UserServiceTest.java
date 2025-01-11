package com.sparta.yobaeats.domain.user.service;

import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateInfoReq;
import com.sparta.yobaeats.domain.user.dto.response.UserReadInfoRes;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(1L)
            .email("testUserMail")
            .password("123ABC!")
            .nickName("testNick")
            .role(UserRole.ROLE_USER)
            .build();
    }

    @DisplayName("토큰의 userId와 요청 userId가 불일치하면, '접근 권한이 없습니다.' 출력")
    @Test
    void validateUserFail() {
        // given
        Long tokenUserId = 2L;

        // when & then
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            userService.validateUser(user.getId(), tokenUserId));
        assertEquals(ErrorCode.UNAUTHORIZED_USER, exception.getErrorCode());
    }

    @DisplayName("권한 검증 성공")
    @Test
    void validateUserSuccess() {
        // given
        Long userId = 1L;
        Long tokenUserId = 1L;

        // when & then
        assertDoesNotThrow(() ->
            userService.validateUser(userId, tokenUserId));
    }

    @DisplayName("존재하지 않는 유저를 조회하면, '유저가 존재하지 않습니다.' 출력")
    @Test
    void findNotExistUser() {
        // given
        Long userId = 2L;
        given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            userService.findUserById(userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @DisplayName("유저 정보 조회 성공")
    @Test
    void findUserSuccess() {
        // given
        Long userId = 1L;
        given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.of(user));

        // when
        UserReadInfoRes res = userService.findById(userId);

        // then
        assertNotNull(res);
        assertEquals(user.getEmail(), res.email());
        assertEquals(user.getNickName(), res.nickName());
    }

    @DisplayName("유저 탈퇴 시 비밀번호가 일치하지 않으면 실패")
    @Test
    void deleteUserWithInvalidPassword() {
        // given
        Long userId = 1L;
        UserDeleteReq req = new UserDeleteReq("wrongPassword");
        given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(req.password(), user.getPassword())).willReturn(false);

        // when & then
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            userService.deleteUser(userId, req));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
        assertFalse(user.isDeleted());
    }

    @DisplayName("유저 softDelete 성공")
    @Test
    void deleteUserSuccess() {
        // given
        Long userId = 1L;
        UserDeleteReq req = new UserDeleteReq("123ABC!");
        given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(req.password(), user.getPassword())).willReturn(true);

        // when
        userService.deleteUser(userId, req);

        // then
        assertTrue(user.isDeleted());
    }

    @DisplayName("수정할 정보가 없을 때 기존 정보 유지")
    @Test
    void updateUserWithNoChanges() {
        // given
        Long userId = 1L;
        UserUpdateInfoReq req = new UserUpdateInfoReq(null, null);
        String originalPassword = user.getPassword();
        String originalNickName = user.getNickName();
        given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.of(user));

        // when
        userService.updateUser(userId, req);

        // then
        assertEquals(originalPassword, user.getPassword());
        assertEquals(originalNickName, user.getNickName());
    }

    @DisplayName("유저 정보 부분 수정 성공")
    @Test
    void updateUserSuccess() {
        // given
        Long userId = 1L;
        String newPassword = "newPassword";
        String encodedPassword = "encodedNewPassword";
        UserUpdateInfoReq req = new UserUpdateInfoReq(newPassword, "newNick");
        given(userRepository.findByIdAndIsDeletedFalse(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);

        // when
        userService.updateUser(userId, req);

        // then
        assertEquals(encodedPassword, user.getPassword());
        assertEquals("newNick", user.getNickName());
    }
}