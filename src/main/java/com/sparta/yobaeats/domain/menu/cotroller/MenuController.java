package com.sparta.yobaeats.domain.menu.controller;

import com.sparta.yobaeats.domain.menu.dto.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Void> createMenu(
            @RequestBody MenuCreateReq menuCreateReq
    ) {
        menuService.createMenu(menuCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(
            @PathVariable("menuId") Long menuId,
            @RequestBody MenuUpdateReq menuUpdateReq
    ) {
        menuService.updateMenu(menuId, menuUpdateReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable("menuId") Long menuId
    ) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }


}
