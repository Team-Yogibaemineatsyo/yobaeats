package com.sparta.yobaeats.domain.menu.controller;

import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.service.MenuService;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성 API
     *
     * @param menuCreateReqList 메뉴 생성 요청 데이터 (JSON 형식)
     *                          - 이름, 가격, 설명 등 메뉴 정보를 포함
     * @return HTTP 201(CREATED) 상태 코드 반환
     */
    @PostMapping
    public ResponseEntity<Void> createMenus(
            @RequestBody @Valid List<MenuCreateReq> menuCreateReqList
    ) {
        List<Long> createdMenuIds = menuService.createMenus(menuCreateReqList);
        URI uri = UriBuilderUtil.create("/api/menus/{menuId}", createdMenuIds.get(0));

        return ResponseEntity.created(uri).build();
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
            @RequestBody @Valid MenuUpdateReq menuUpdateReq
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
