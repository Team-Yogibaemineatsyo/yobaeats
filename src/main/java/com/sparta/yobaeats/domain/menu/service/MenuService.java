package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
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
     * 여러 개의 메뉴를 생성합니다.
     *
     * @param menuCreateReq 생성할 메뉴의 요청 데이터 리스트
     * @param userId 메뉴를 생성하는 사용자의 ID
     * @return 생성된 메뉴의 ID 리스트
     */
    public List<Long> createMenus(MenuCreateReq menuCreateReq, Long userId) {
                    // 스토어 조회
                    Store store = storeService.findStoreById(menuCreateReq.storeId());

                    // 소유자 체크
                    userService.validateUser(store.getUser().getId(), userId);

        List<Menu> menus = menuCreateReq.menus()
                .stream()
                .map(menuReq -> {
                    // Store 엔티티를 사용하여 List<Menu> 생성
                    return menuReq.toEntity(store);
                })
                .toList();

        List<Menu> savedMenus = menuRepository.saveAll(menus);

        return savedMenus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴를 수정합니다.
     *
     * @param menuId 수정할 메뉴의 ID
     * @param menuUpdateReq 수정 요청 데이터
     * @param userId 메뉴를 수정하는 사용자의 ID
     */
    public void updateMenu(Long menuId, MenuUpdateReq menuUpdateReq, Long userId) {
        Menu menu = findMenuById(menuId);

        // 스토어 객체 가져오기
        Store store = menu.getStore();

        // 소유자 체크
        userService.validateUser(store.getUser().getId(), userId);

        menu.update(
                menuUpdateReq.menuName(),
                menuUpdateReq.menuPrice(),
                menuUpdateReq.description()
        );
    }

    /**
     * 메뉴를 삭제합니다.
     *
     * @param menuId 삭제할 메뉴의 ID
     * @param userId 메뉴를 삭제하는 사용자의 ID
     */
    public void deleteMenu(Long menuId, Long userId) {
        Menu menu = findMenuById(menuId);

        // 스토어 객체 가져오기
        Store store = menu.getStore();

        // 소유자 체크
        userService.validateUser(store.getUser().getId(), userId);

        menu.markAsDeleted();
        menuRepository.save(menu);
    }

    /**
     * 메뉴 ID로 메뉴를 조회합니다.
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
