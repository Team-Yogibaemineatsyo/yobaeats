package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 메뉴 관리 로직을 처리하는 Service 클래스
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createMenu(MenuCreateReq menuCreateReq) {
        // Store ID를 통해 가게 정보 조회
        Store store = storeRepository.findById(menuCreateReq.storeId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND));

        // DTO의 팩토리 메서드를 사용해 Menu 엔티티 생성
        Menu menu = menuCreateReq.toEntity(store);

        // 메뉴 저장
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(Long menuId, MenuUpdateReq menuUpdateReq) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));

        Menu updatedMenu = Menu.update(menu,
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description()
        );
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));

        menu.markAsDeleted();
    }
}
