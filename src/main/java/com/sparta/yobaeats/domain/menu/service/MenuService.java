package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;
    private final UserService userService;

    /**
     * 메뉴 여러 개 생성 메서드
     */
    public List<Long> createMenus(List<MenuCreateReq> menuCreateReqList) {
        List<Menu> menus = menuCreateReqList.stream()
                .map(menuCreateReq -> {
                    Store store = storeService.findStoreById(menuCreateReq.storeId());

                    // 소유자 체크
                    checkIfOwner(store.getUser().getId());

                    return menuCreateReq.toEntity(store);
                })
                .collect(Collectors.toList());

        List<Menu> savedMenus = menuRepository.saveAll(menus);

        return savedMenus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 수정 메서드
     */
    public void updateMenu(Long menuId, MenuUpdateReq menuUpdateReq) {
        Menu menu = findMenuById(menuId);

        // 소유자 체크
        checkIfOwner(menu.getStore().getUser().getId());

        menu.update(
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description()
        );
    }

    /**
     * 메뉴 삭제 메서드
     */
    public void deleteMenu(Long menuId) {
        Menu menu = findMenuById(menuId);

        // 소유자 체크
        checkIfOwner(menu.getStore().getUser().getId());

        menu.markAsDeleted();
        menuRepository.save(menu);
    }

    /**
     * 현재 인증된 사용자의 ID를 기반으로 소유자 체크
     */
    private void checkIfOwner(Long ownerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE); // 인증되지 않은 사용자
        }

        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
        String authenticatedUserEmail = userDetails.getUsername(); // 사용자 이메일 가져오기

        // 이메일로 사용자 ID를 찾기
        User authenticatedUser = userService.findUserByEmail(authenticatedUserEmail);
        Long authenticatedUserId = authenticatedUser.getId(); // 사용자 ID 가져오기

        // 소유자 ID와 비교
        if (!ownerId.equals(authenticatedUserId)) {
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_USER); // 권한 없는 사용자
        }
    }

    /**
     * 메뉴 ID로 메뉴 조회
     */
    public Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));
    }

}