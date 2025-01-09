package com.sparta.yobaeats.domain.menu.controller;

import com.sparta.yobaeats.domain.menu.dto.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성 API
     *
     * @param menuCreateReq 메뉴 생성 요청 데이터 (JSON 형식)
     *                       - 이름, 가격, 설명 등 메뉴 정보를 포함
     * @return HTTP 201(CREATED) 상태 코드 반환
     */
    @PostMapping
    public ResponseEntity<Void> createMenu(
            @Valid @RequestBody MenuCreateReq menuCreateReq
    ) {
        menuService.createMenu(menuCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 메뉴 수정 API
     *
     * @param menuId 수정할 메뉴의 ID (경로 변수)
     * @param menuUpdateReq 메뉴 수정 요청 데이터 (JSON 형식)
     *                       - 변경할 이름, 가격, 설명 등을 포함
     * @return HTTP 200(OK) 상태 코드 반환
     */
    @PatchMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(
            @PathVariable("menuId") Long menuId,
            @Valid @RequestBody MenuUpdateReq menuUpdateReq
    ) {
        menuService.updateMenu(menuId, menuUpdateReq);
        return ResponseEntity.ok().build();
    }

    /**
     * 메뉴 삭제 API
     *
     * @param menuId 삭제할 메뉴의 ID (경로 변수)
     * @return HTTP 204(NO CONTENT) 상태 코드 반환
     *         - 성공적으로 삭제되었음을 의미
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable("menuId") Long menuId
    ) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}
