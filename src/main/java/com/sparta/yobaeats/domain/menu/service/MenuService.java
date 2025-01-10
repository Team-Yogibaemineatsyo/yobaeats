package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
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
     * 메뉴 여러 개를 생성하는 메서드
     *
     * @param menuCreateReqList 생성할 메뉴의 요청 데이터 리스트
     * @return 생성된 메뉴의 ID 리스트
     */
    public List<Long> createMenus(List<MenuCreateReq> menuCreateReqList, UserDetailsCustom userDetails) {
        List<Menu> menus = menuCreateReqList.stream()
                .map(menuCreateReq -> {
                    Store store = storeService.findStoreById(menuCreateReq.storeId());

                    // 인증된 사용자 ID 가져오기
                    Long userId = userDetails.getId();
                    // 사용자 객체 조회
                    User user = userService.findUserById(userId); // User 객체를 가져옴

                    // 소유자 체크
                    checkIfOwner(userDetails);

                    return menuCreateReq.toEntity(store);
                })
                .collect(Collectors.toList());

        List<Menu> savedMenus = menuRepository.saveAll(menus);

        return savedMenus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴를 수정하는 메서드
     *
     * @param menuId 수정할 메뉴의 ID
     * @param menuUpdateReq 수정 요청 데이터
     */
    public void updateMenu(Long menuId, MenuUpdateReq menuUpdateReq, UserDetailsCustom userDetails) {
        Menu menu = findMenuById(menuId);

        // 인증된 사용자 ID 가져오기
        Long userId = userDetails.getId();
        // 사용자 객체 조회
        User user = userService.findUserById(userId); // User 객체를 가져옴

        // 소유자 체크
        checkIfOwner(userDetails);

        menu.update(
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description()
        );
    }

    /**
     * 메뉴를 삭제하는 메서드
     *
     * @param menuId 삭제할 메뉴의 ID
     */
    public void deleteMenu(Long menuId, UserDetailsCustom userDetails) {
        Menu menu = findMenuById(menuId);

        // 인증된 사용자 ID 가져오기
        Long userId = userDetails.getId();
        // 사용자 객체 조회
        User user = userService.findUserById(userId); // User 객체를 가져옴

        // 소유자 체크
        checkIfOwner(userDetails);

        menu.markAsDeleted();
        menuRepository.save(menu);
    }

    /**
     * 현재 로그인된 사용자가 사장님(ROLE_OWNER)인지 확인하는 메서드
     * 인증된 사용자가 사장님 역할인지 검증하며, 아닐 경우 예외를 발생시킴
     */
    private void checkIfOwner(UserDetailsCustom userDetails) {
        // 인증되지 않은 사용자 확인
        if (userDetails == null) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE); // 인증되지 않은 사용자
        }

        // 사용자의 권한을 확인하고 ROLE_OWNER인지 검증
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE))
                .getAuthority();

        // ROLE_OWNER인 경우만 권한 허용
        if (!UserRole.ROLE_OWNER.name().equals(role)) {
            throw new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE);  // 권한이 없는 경우 예외 처리
        }
    }


    /**
     * 메뉴 ID로 메뉴를 조회하는 메서드
     *
     * @param menuId 조회할 메뉴의 ID
     * @return 조회된 메뉴 객체
     * @throws CustomRuntimeException 메뉴를 찾을 수 없는 경우 발생
     */
    public Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.MENU_NOT_FOUND));
    }
}
