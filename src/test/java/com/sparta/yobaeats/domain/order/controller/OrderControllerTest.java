package com.sparta.yobaeats.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.yobaeats.domain.order.dto.request.OrderCreateReq;
import com.sparta.yobaeats.domain.order.service.OrderService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.global.jwt.JwtUtil;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import jakarta.transaction.Transactional;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    private static final String BASE_URL = "/api/orders";

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @Test
    void 주문_생성_성공() throws Exception {
        // given
        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_USER) // ROLE_USER로 수정
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        OrderCreateReq orderRequest = new OrderCreateReq(1L);
        String orderJson = objectMapper.writeValueAsString(orderRequest);

        // JWT 토큰 생성
        String token = jwtUtil.generateTokenByAuthentication(authentication);
        System.out.println("Generated Token: " + token); // 토큰 출력하여 확인

        // createOrder 메서드가 Long을 반환하도록 설정
        Long mockOrderId = 1L;
        when(orderService.createOrder(any(OrderCreateReq.class), eq(user.getId()))).thenReturn(mockOrderId); // user.getId()로 변경

        // when
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderJson)
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        verify(orderService, times(1)).createOrder(any(OrderCreateReq.class), eq(user.getId())); // user.getId()로 변경
    }


    @Test
    void 주문_상태_변경_성공() throws Exception {
        // given
        Long orderId = 1L;
        Long userId = 1L;

        // 오너 사용자 설정
        User owner = User.builder()
                .id(userId)
                .email("owner@example.com")
                .password("password")
                .nickName("ownername")
                .role(UserRole.ROLE_OWNER) // 오너 역할로 설정
                .build();

        CustomUserDetails ownerDetails = new CustomUserDetails(owner);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                ownerDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_OWNER")) // 오너 권한 설정
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/orders/{orderId}", orderId)
                        .header("Authorization", "Bearer sample-token"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).updateOrderStatus(orderId, userId);
    }
}