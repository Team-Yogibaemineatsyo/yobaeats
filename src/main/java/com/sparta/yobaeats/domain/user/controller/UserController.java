package com.sparta.yobaeats.domain.user.controller;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.response.UserReadInfoRes;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateInfoReq;
import com.sparta.yobaeats.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserReadInfoRes> findUserById(
        @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        Long userId = userDetails.getId();
        UserReadInfoRes userRes = userService.findById(userId);
        return ResponseEntity.ok(userRes);
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(
        @RequestBody @Valid UserUpdateInfoReq req,
        @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        Long userId = userDetails.getId();
        userService.updateUser(userId, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
        @RequestBody @Valid UserDeleteReq req,
        @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        Long userId = userDetails.getId();
        userService.deleteUser(req, userId);
        return ResponseEntity.noContent().build();
    }
}
