package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    // private final StoreService storeService; // StoreService 의존성 주석 처리

    @Transactional
    public void createMenu(MenuCreateReq menuCreateReq) {
        // Store ID를 직접 사용하여 Menu 생성(병합 후 삭제)
        Long storeId = menuCreateReq.storeId();

        // 병합하고 주석풀기
        // Store store = storeService.getStoreById(menuCreateReq.getStoreId());

        Menu menu = Menu.builder()
                //.store(store) 병합 후 주석풀기
                .storeId(storeId)  // Store ID로 초기화(병합 후 삭제)
                .menuName(menuCreateReq.menuName())
                .menuPrice(menuCreateReq.menuPrice())
                .description(menuCreateReq.description())
                .build();


        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(Long menuId, MenuUpdateReq menuUpdateReq) {
        // 메뉴를 ID로 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND)); // 메뉴가 없을 경우 예외 처리

        Menu updatedMenu = Menu.update(menu,
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description());
    }
}
