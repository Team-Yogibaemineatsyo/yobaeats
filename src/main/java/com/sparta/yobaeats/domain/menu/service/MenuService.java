package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.UserRole;
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
    private final StoreService storeService;  // StoreService 의존성 주입

    // 현재 로그인된 사용자가 사장님(ROLE_OWNER)인지 확인하는 메서드
    private void checkIfOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE))
                .getAuthority();

        // ROLE_OWNER인 경우만 권한 허용
        if (!UserRole.ROLE_OWNER.name().equals(role)) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE);  // 권한이 없는 경우 예외 처리
        }
    }

    /**
     * 메뉴 여러 개 생성 메서드
     */
    public List<Long> createMenus(List<MenuCreateReq> menuCreateReqList) {
        checkIfOwner();  // 메뉴 생성 전 권한 체크
        // 메뉴 목록을 순회하면서 각 메뉴에 대해 store 정보 조회 후 Menu 엔티티 생성
        List<Menu> menus = menuCreateReqList.stream()
                .map(menuCreateReq -> {
                    // Store 정보 조회 및 예외 처리
                    Store store = storeService.findStoreById(menuCreateReq.storeId());  // StoreService의 메서드 사용

                    // Menu 엔티티 생성
                    return menuCreateReq.toEntity(store);
                })
                .collect(Collectors.toList());

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
        checkIfOwner();  // 메뉴 수정 전 권한 체크
        Menu menu = findMenuById(menuId);  // 메뉴를 ID로 찾는 메서드 호출

        // 메뉴 업데이트
        Menu.update(menu,
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description()
        );
    }

    /**
     * 메뉴 삭제 메서드
     */
    public void deleteMenu(Long menuId) {
        checkIfOwner();  // 메뉴 수정 전 권한 체크
        Menu menu = findMenuById(menuId);  // 메뉴를 ID로 찾는 메서드 호출

        // 메뉴 삭제 처리
        menu.delete();  // delete() 메서드는 이미 삭제된 메뉴일 경우 ConflictException을 던짐

        // 메뉴를 삭제 상태로 설정
        menu.markAsDeleted();

        // 변경된 메뉴 상태 저장
        menuRepository.save(menu);
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
