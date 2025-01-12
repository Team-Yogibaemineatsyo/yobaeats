package com.sparta.yobaeats.domain.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuInfoCreateReq;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuControllerTest {

    private static final String BASE_URL = "/api/menus";

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User owner;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .email("owner@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_OWNER)
                .build();
    }

    private String setAuthentication() {
        CustomUserDetails ownerDetails = new CustomUserDetails(owner);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                ownerDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_OWNER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        return jwtUtil.generateTokenByAuthentication(authentication);
    }

    @Test
    void 메뉴_생성_성공() throws Exception {
        // given
        String token = setAuthentication(); // 오너로 인증 설정

        MenuCreateReq menuCreateReq = new MenuCreateReq(1L, List.of(
                new MenuInfoCreateReq("치킨", 18000, "치킨은 항상 옳다"),
                new MenuInfoCreateReq("피자", 22000, "치즈가 듬뿍 담긴 피자"),
                new MenuInfoCreateReq("햄버거", 15000, "맛있는 햄버거"),
                new MenuInfoCreateReq("냉면", 15000, "시원한 냉면")
        ));

        String menuJson = objectMapper.writeValueAsString(menuCreateReq); // MenuCreateReq 객체로 변환

        // Mock 설정: menuService.createMenus가 호출될 때 하드코딩된 메뉴 ID 리스트를 반환하도록 설정
        when(menuService.createMenus(any(MenuCreateReq.class), any(Long.class))).thenReturn(List.of(1L, 2L, 3L, 4L));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/1")); // Optional: 생성된 리소스의 위치 확인

        verify(menuService, times(1)).createMenus(any(MenuCreateReq.class), eq(owner.getId()));
    }

//    @Test
//    void 메뉴_수정_성공() throws Exception {
//        // given
//        String token = setAuthentication(); // 오너로 인증 설정
//        Long menuId = 1L;
//        Long ownerId = owner.getId();
//
//        MenuUpdateReq updateRequest = new MenuUpdateReq(
//                "맛있는 치킨", // 메뉴 이름
//                20000,         // 메뉴 가격
//                "더욱 맛있어진 치킨" // 메뉴 설명
//        );
//
//        String updateJson = objectMapper.writeValueAsString(updateRequest);
//
//        // when & then
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/menus/{menuId}", menuId)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
//                        .content(updateJson)) // 요청 본문 추가
//                .andExpect(status().isNoContent());
//
//        // 메뉴 수정 메서드 호출 검증
//        verify(menuService, times(1)).updateMenu(eq(menuId), any(MenuUpdateReq.class), eq(ownerId));
//    }


    @Test
    void 메뉴_삭제_성공() throws Exception {
        // given
        String token = setAuthentication(); // 오너로 인증 설정

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/menus/{menuId}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        verify(menuService, times(1)).deleteMenu(eq(1L), eq(1L));
    }
}
