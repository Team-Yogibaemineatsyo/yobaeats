package com.sparta.yobaeats.domain.user.controller;

import com.sparta.yobaeats.domain.user.dto.request.UserDeleteReq;
import com.sparta.yobaeats.domain.user.dto.response.UserRes;
import com.sparta.yobaeats.domain.user.dto.request.UserUpdateReq;
import com.sparta.yobaeats.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserRes> findUserById(
        // @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long userId
    ) {
        // userId? email?
        // String email =userDetails.getId(); ( 일단 대충..나중에 시큐리티 보고 수정 )
        UserRes userRes = userService.findById(userId);
        return ResponseEntity.ok(userRes);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
        @Valid @RequestBody UserUpdateReq req,
        @PathVariable Long userId
        // @AuthenticationPrincipal UserDetails userDetails

    ) {
        // userId? email?
        // String email = userDetails.getId(); ( 일단 대충..나중에 시큐리티 보고 수정 )
        userService.updateUser(userId, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
        @Valid @RequestBody UserDeleteReq req,
        @PathVariable Long userId
        // @AuthenticationPrincipal UserDetails userDetails
    ) {
        // userId? email?
        // String email = userDetails.getId(); ( 일단 대충..나중에 시큐리티 보고 수정 )
        userService.deleteUser(req, userId);
        return ResponseEntity.noContent().build();
    }
}
