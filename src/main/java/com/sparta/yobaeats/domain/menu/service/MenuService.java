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
    // private final StoreService storeService; // 병합 후 주석풀기

    @Transactional
    public void createMenu(MenuCreateReq menuCreateReq) {
        Long storeId = menuCreateReq.storeId();

        Menu menu = Menu.builder()
                .storeId(storeId)
                .menuName(menuCreateReq.menuName())
                .menuPrice(menuCreateReq.menuPrice())
                .description(menuCreateReq.description())
                .build();


        menuRepository.save(menu);
    }

    // 병합 후 사용할 메서드
//    @Transactional
//    public void createMenu(MenuCreateReq menuCreateReq) {
//        // Store ID를 통해 가게 정보 조회
//        Store store = storeRepository.findById(menuCreateReq.storeId())
//                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.STORE_NOT_FOUND)); // 가게가 없을 경우 예외 처리
//
//        Menu menu = Menu.builder()
//                .store(store) // Store 객체로 초기화
//                .menuName(menuCreateReq.menuName())
//                .menuPrice(menuCreateReq.menuPrice())
//                .description(menuCreateReq.description())
//                .build();
//
//        menuRepository.save(menu);
//    }

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

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND)); // 메뉴가 없을 경우 예외 처리
        menu.markAsDeleted();
    }
}
