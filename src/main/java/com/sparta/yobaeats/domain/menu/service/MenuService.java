package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
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
        Long storeId = menuCreateReq.getStoreId();

        // 병합하고 주석풀기
        // Store store = storeService.getStoreById(menuCreateReq.getStoreId());

        Menu menu = Menu.builder()
                //.store(store) 병합 후 주석풀기
                .storeId(storeId) // Store ID로 초기화(병합 후 삭제)
                .menuName(menuCreateReq.getMenuName())
                .menuPrice(menuCreateReq.getMenuPrice())
                .description(menuCreateReq.getDescription())
                .build();

        menuRepository.save(menu);
    }
}
