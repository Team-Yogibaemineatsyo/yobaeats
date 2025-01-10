package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.global.exception.CustomRuntimeException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreService storeService;

    // 메뉴 여러 개 생성 테스트
    @Test
    @WithMockUser(roles = "OWNER", username = "owner@example.com")
    void 메뉴_여러개_생성_성공() {
        Long storeId = 1L;
        User owner = User.builder()
                .id(1L) // 적절한 ID 설정
                .email("owner@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_OWNER)
                .build();

        Store store = Store.builder()
                .id(storeId)
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0)
                .isDeleted(false)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(21, 0))
                .user(owner)
                .build();


        MenuCreateReq menuCreateReq1 = new MenuCreateReq(storeId, "메뉴 이름 1", 10000, "메뉴 설명 1");
        MenuCreateReq menuCreateReq2 = new MenuCreateReq(storeId, "메뉴 이름 2", 12000, "메뉴 설명 2");
        List<MenuCreateReq> menuCreateReqList = Arrays.asList(menuCreateReq1, menuCreateReq2);

        when(storeService.findStoreById(storeId)).thenReturn(store);

        Menu menu1 = new Menu(1L, store, "메뉴 이름 1", 10000, "메뉴 설명 1", false);
        Menu menu2 = new Menu(2L, store, "메뉴 이름 2", 12000, "메뉴 설명 2", false);
        when(menuRepository.saveAll(anyList())).thenReturn(Arrays.asList(menu1, menu2));

        // when
        List<Long> menuIds = menuService.createMenus(menuCreateReqList);

        // then
        verify(menuRepository).saveAll(anyList());
        assertEquals(2, menuIds.size());
        assertEquals(1L, menuIds.get(0));
        assertEquals(2L, menuIds.get(1));
    }

    // 메뉴 수정 테스트
    @Test
    @WithMockUser(roles = "OWNER")
    void 메뉴_수정_성공() {
        // given
        Long menuId = 1L;
        Long storeId = 1L; // 가게 ID 추가
        User owner = User.builder()
                .id(1L) // 소유자 ID 설정
                .email("owner@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_OWNER)
                .build();

        Store store = Store.builder()
                .id(storeId)
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0)
                .isDeleted(false)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(21, 0))
                .user(owner) // 소유자 설정
                .build(); // Store 객체 생성

        Menu existingMenu = new Menu(menuId, store, "Old Name", 10000, "Old Description", false);

        MenuUpdateReq updateReq = new MenuUpdateReq("New Name", 12000, "New Description");

        // 모의 객체 설정
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));
        when(storeService.findStoreById(storeId)).thenReturn(store); // 가게 정보 조회를 추가

        // when
        menuService.updateMenu(menuId, updateReq); // 메뉴 수정 호출

        // then
        assertEquals("New Name", existingMenu.getMenuName()); // 이름 확인
        assertEquals(12000, existingMenu.getMenuPrice()); // 가격 확인
        assertEquals("New Description", existingMenu.getDescription()); // 설명 확인
    }


    // 메뉴 삭제 테스트
    @Test
    @WithMockUser(roles = "OWNER")
    void 메뉴_삭제_성공() {
        // given
        Long menuId = 1L;
        Long storeId = 1L; // 가게 ID 추가
        User owner = User.builder()
                .id(1L) // 소유자 ID 설정
                .email("owner@example.com")
                .password("password")
                .nickName("username")
                .role(UserRole.ROLE_OWNER)
                .build();

        Store store = Store.builder()
                .id(storeId)
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0)
                .isDeleted(false)
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(21, 0))
                .user(owner) // 소유자 설정
                .build(); // Store 객체 생성

        Menu existingMenu = new Menu(menuId, store, "Menu to be deleted", 10000, "Description", false);

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));
        when(storeService.findStoreById(storeId)).thenReturn(store); // 가게 정보 조회를 추가

        // when
        menuService.deleteMenu(menuId);

        // then
        verify(menuRepository).save(existingMenu); // 메뉴를 삭제하기 위해 save 호출 (비활성화 처리)
        assertTrue(existingMenu.isDeleted()); // 삭제 플래그가 true로 설정되었는지 확인
    }

    // 메뉴 수정 실패 - 메뉴가 존재하지 않음
    @Test
    @WithMockUser(roles = "OWNER")
    void 메뉴_수정_실패_메뉴_존재하지않음() {
        // given
        Long menuId = 1L;
        MenuUpdateReq updateReq = new MenuUpdateReq("New Name", 12000, "New Description");

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        // when & then
        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
            menuService.updateMenu(menuId, updateReq);
        });
        assertEquals(ErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
    }

    // 메뉴 삭제 실패 - 메뉴가 존재하지 않음
    @Test
    @WithMockUser(roles = "OWNER")
    void 메뉴_삭제_실패_메뉴_존재하지않음() {
        // given
        Long menuId = 1L;

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        // when & then
        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
            menuService.deleteMenu(menuId);
        });
        assertEquals(ErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
    }
}
