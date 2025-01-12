package com.sparta.yobaeats.domain.user.controller;

import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateInfoReq;
import com.sparta.yobaeats.domain.user.dto.response.UserReadInfoRes;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private JwtUtil jwtUtil;

    private User dummyUser;
    private CustomUserDetails customUserDetails;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        dummyUser = new User(1L, "user1@mail.com", "12345!", "유저", UserRole.ROLE_USER);
        customUserDetails = new CustomUserDetails(dummyUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities()
        );

        jwtToken = jwtUtil.generateTokenByAuthentication(authentication);
        ;

        SecurityContextHolder.getContext().setAuthentication(authentication);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
    }

    @DisplayName("유저는 자기 자신의 정보를 조회한다.")
    @Test
    void readUserInfo() throws Exception {
        // given
        UserReadInfoRes userReadInfoRes = UserReadInfoRes.from(dummyUser);
        given(userService.findById(dummyUser.getId())).willReturn(userReadInfoRes);

        // when & then
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(user(customUserDetails)))
                .andExpect(jsonPath("$.userId").value(dummyUser.getId()))
                .andExpect(jsonPath("$.email").value(dummyUser.getEmail()))
                .andExpect(jsonPath("$.nickName").value(dummyUser.getNickName()))
                .andExpect(jsonPath("$.userRole").value(dummyUser.getRole().toString()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저는 자기 자신의 정보를 수정한다.")
    @Test
    void updateUserInfo() throws Exception {
        // given
        UserUpdateInfoReq userUpdateInfoReq = new UserUpdateInfoReq("newPassword1!", "newNickName");

        // when & then
        mockMvc.perform(patch("/api/users/me")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"password\" :  \"newPassword1!\", \"nickName\" : \"newNickName\" }")
                        .with(user(customUserDetails)))
                .andExpect(status().isNoContent());

        verify(userService).updateUser(dummyUser.getId(), userUpdateInfoReq);
    }

    @DisplayName("유저는 자기 자신의 정보를 삭제한다.")
    @Test
    void deleteUser() throws Exception {
        // given
        UserDeleteReq userDeleteReq = new UserDeleteReq("newPassword1!");

        // when & then
        mockMvc.perform(delete("/api/users/me")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"password\" :  \"newPassword1!\" }")
                        .with(user(customUserDetails)))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(dummyUser.getId(), userDeleteReq);
    }
}