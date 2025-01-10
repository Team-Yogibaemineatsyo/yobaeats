package com.sparta.yobaeats.domain.auth.service;

import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[/api/auth] - 서비스 단위 테스트")
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock BCryptPasswordEncoder passwordEncoder;
    @InjectMocks AuthService authService;

    String email = "test@test.com";
    String plainPassword = "plainPassword";
    String nick = "nick";
    String role = "ROLE_USER";

    String hashedPassword = "hashedPassword";

    @Nested
    @DisplayName("[/signup] - 회원가입")
    class SignUpTest {

        @Test
        @DisplayName("[성공] - 회원가입에 성공하면 해당 유저와 동일한 엔티티 조회 가능")
        void signup_user_create_success() {
            // given
            AuthSignupRequest request = new AuthSignupRequest(email, plainPassword, nick, role);
            User requestedUser = request.toEntity(hashedPassword);
            User createdUser = new User(1L, email, hashedPassword, nick, UserRole.of(role));

            when(userRepository.existsByEmail(email)).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(createdUser);

            // when
            Long resultId = authService.signup(request);

            // then
            assertThat(requestedUser)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(createdUser);

            assertThat(createdUser.getId()).isEqualTo(resultId);

            verify(userRepository, times(1)).existsByEmail(email);
            verify(passwordEncoder, times(1)).encode(plainPassword);
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("[실패] - 사용중인 이메일로 회원가입 시 실패")
        void signup_encode_password_success() {
            // given
            AuthSignupRequest request = new AuthSignupRequest(email, plainPassword, nick, role);
            ErrorCode errorCode = ErrorCode.USER_EMAIL_ALREADY_EXISTS;

            when(userRepository.existsByEmail(email)).thenReturn(true);

            // when
            ConflictException existsEmailEx =
                    Assertions.assertThrows(ConflictException.class, () -> authService.signup(request));

            // then
            assertThat(existsEmailEx.getErrorCode().getStatus()).isEqualTo(errorCode.getStatus());
            assertThat(existsEmailEx.getErrorCode().getMessage()).isEqualTo(errorCode.getMessage());

            verify(userRepository, times(1)).existsByEmail(email);
            verify(passwordEncoder, times(0)).encode(any(String.class));
            verify(userRepository, times(0)).save(any(User.class));
        }
    }
}