package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 메뉴 관리 로직을 처리하는 Service 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;

    /**
     * 메뉴 여러 개 생성 메서드
     */
    public List<Long> createMenus(List<MenuCreateReq> menuCreateReqList) {
        // 메뉴 목록을 순회하면서 각 메뉴에 대해 store 정보 조회 후 Menu 엔티티 생성
        List<Menu> menus = menuCreateReqList.stream()
                .map(menuCreateReq -> {
                    // Store 정보 조회 및 예외 처리
                    Store store = storeService.findStoreById(menuCreateReq.storeId());  // StoreService의 메서드 사용

                    // Menu 엔티티 생성
                    return menuCreateReq.toEntity(store);
                })
                .collect(Collectors.toList());

        // 각 메뉴에 대해 소유자 체크
        for (Menu menu : menus) {
            checkIfOwner(menu.getStore().getId(), menu.getStore().getUser().getId());
        }

        // 여러 개의 메뉴를 한 번에 저장하고, 저장된 엔티티 목록 반환
        List<Menu> savedMenus = menuRepository.saveAll(menus);

        // 저장된 메뉴들의 ID 목록을 반환
        return savedMenus.stream()
                .map(Menu::getId)  // 각 메뉴의 ID를 추출
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 수정 메서드
     */
    public void updateMenu(Long menuId, MenuUpdateReq menuUpdateReq) {
        // 메뉴를 ID로 찾는 메서드 호출
        Menu menu = findMenuById(menuId);

        // 소유자 체크
        checkIfOwner(menu.getStore().getId(), menu.getStore().getUser().getId());

        // 메뉴 업데이트
        menu.update(  // 기존의 메뉴 객체를 사용하여 업데이트
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description()
        );
    }

    /**
     * 메뉴 삭제 메서드
     */
    public void deleteMenu(Long menuId) {
        Menu menu = findMenuById(menuId);  // 메뉴를 ID로 찾는 메서드 호출

        // 소유자 체크
        checkIfOwner(menu.getStore().getId(), menu.getStore().getUser().getId());

        // 메뉴 삭제 처리
        menu.delete();  // delete() 메서드는 이미 삭제된 메뉴일 경우 ConflictException을 던짐
        menu.markAsDeleted(); // 삭제 상태로 설정
        menuRepository.save(menu); // 변경된 메뉴 상태 저장
    }

    public void checkIfOwner(Long storeId, Long userId) {
        Store store = storeService.findStoreById(storeId);
        if (!store.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE);
        }
    }

    /**
     * 메뉴 ID로 메뉴를 조회하는 메서드
     */
    public Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));  // 메뉴가 없으면 예외 처리
    }

    /**
     * 가게 정보 조회 시 삭제되지 않은 메뉴만 반환
     */
    public List<Menu> findMenusByStoreIdAndIsDeletedFalse(Long storeId) {
        return menuRepository.findByStoreIdAndIsDeletedFalse(storeId);
    }
}
