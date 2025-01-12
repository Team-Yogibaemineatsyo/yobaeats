 package com.sparta.yobaeats.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateInfoReq;
import com.sparta.yobaeats.domain.user.dto.response.UserReadInfoRes;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.repository.UserRepository;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private User dummyUser;

    @BeforeEach
    void setUp() {
        AuthSignupRequest request = new AuthSignupRequest("user1@mail.com", "12345!", "유저", UserRole.ROLE_OWNER.name());
        dummyUser = request.toEntity(passwordEncoder.encode(request.password()));

        userRepository.save(dummyUser);
    }

    @DisplayName("유저는 자기 자신의 정보를 조회한다.")
    @Test
    void readUserInfo() throws Exception {
        // given
        UserReadInfoRes userReadInfoRes = UserReadInfoRes.from(dummyUser);

        // when & then
        mockMvc.perform(get("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", generateTestJwtToken()))
                .andExpect(jsonPath("$.userId").value(dummyUser.getId()))
                .andExpect(jsonPath("$.email").value(dummyUser.getEmail()))
                .andExpect(jsonPath("$.nickName").value(dummyUser.getNickName()))
                .andExpect(jsonPath("$.userRole").value(UserRole.ROLE_OWNER.name()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저는 자기 자신의 정보를 수정한다.")
    @Test
    void updateUserInfo() throws Exception {
        // given
        UserUpdateInfoReq userUpdateInfoReq = new UserUpdateInfoReq("newPassword1!", "newNickName");
        String requestBody = objectMapper.writeValueAsString(userUpdateInfoReq);

        // when & then
        mockMvc.perform(patch("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", generateTestJwtToken())
                        .content(requestBody))
                .andExpect(status().isNoContent())
                .andDo(print());

        User updatedUser = userRepository.findById(dummyUser.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches("newPassword1!", updatedUser.getPassword()));
        assertEquals("newNickName", updatedUser.getNickName());
    }

    @DisplayName("유저는 자기 자신의 정보를 삭제한다.")
    @Test
    void deleteUser() throws Exception {
        // given
        UserDeleteReq userDeleteReq = new UserDeleteReq("12345!");
        String requestBody = objectMapper.writeValueAsString(userDeleteReq);

        // when & then
        mockMvc.perform(delete("/api/users/me")
                        .header("Authorization", generateTestJwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());


        User deletedUser = userRepository.findById(dummyUser.getId()).orElseThrow();
        assertTrue(deletedUser.isDeleted());
    }

    private String generateTestJwtToken() {
        CustomUserDetails customUserDetails = new CustomUserDetails(dummyUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        return jwtUtil.generateTokenByAuthentication(authentication);
    }
}