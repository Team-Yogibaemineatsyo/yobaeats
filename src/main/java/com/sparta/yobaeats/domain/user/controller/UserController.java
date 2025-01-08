package com.sparta.yobaeats.domain.user.controller;

import com.sparta.yobaeats.domain.user.dto.UserDeleteReq;
import com.sparta.yobaeats.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
        @RequestBody UserDeleteReq req,
        @PathVariable Long userId
    ) {
        userService.deleteUser(req, userId);
        return ResponseEntity.noContent().build();
    }
}
