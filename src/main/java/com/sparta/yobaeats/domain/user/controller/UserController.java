package com.sparta.yobaeats.domain.user.controller;

import com.sparta.yobaeats.domain.user.dto.UserRes;
import com.sparta.yobaeats.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserRes> findUserById(
        // @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long userId
    ) {
        // userId? email?
        // String email =userDetails.getId(); ( 일단 대충..나중에 시큐리티 보고 수정 )
        UserRes userRes = userService.findUserById(userId);
        return ResponseEntity.ok(userRes);
    }
}
