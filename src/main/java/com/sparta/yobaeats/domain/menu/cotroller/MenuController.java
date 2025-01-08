package com.sparta.yobaeats.domain.menu.controller;

import com.sparta.yobaeats.domain.menu.dto.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Void> createMenu(@RequestBody MenuCreateReq menuCreateReq) {
        menuService.createMenu(menuCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
