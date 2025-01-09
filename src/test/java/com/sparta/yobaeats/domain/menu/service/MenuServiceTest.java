//package com.sparta.yobaeats.domain.menu.service;
//
//import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
//import com.sparta.yobaeats.domain.menu.dto.request.MenuUpdateReq;
//import com.sparta.yobaeats.domain.menu.entity.Menu;
//import com.sparta.yobaeats.domain.store.entity.Store;
//import com.sparta.yobaeats.domain.store.repository.StoreRepository;
//import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
//import com.sparta.yobaeats.global.exception.CustomRuntimeException;
//import com.sparta.yobaeats.global.exception.error.ErrorCode;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.context.SecurityContext;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class MenuServiceTest {
//
//    @InjectMocks
//    private MenuService menuService;
//
//    @Mock
//    private MenuRepository menuRepository;
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    // Create multiple menus test
//    @Test
//    void 메뉴_여러개_생성_성공() {
//        // given
//        Long storeId = 1L;
//        Store store = Store.builder()
//                .id(storeId)
//                .name("가게 이름")
//                .minOrderPrice(5000)
//                .starRate(0.0)
//                .isDeleted(false)
//                .build();
//
//        MenuCreateReq menuCreateReq1 = new MenuCreateReq(storeId, "메뉴 이름 1", 10000, "메뉴 설명 1");
//        MenuCreateReq menuCreateReq2 = new MenuCreateReq(storeId, "메뉴 이름 2", 12000, "메뉴 설명 2");
//        List<MenuCreateReq> menuCreateReqList = Arrays.asList(menuCreateReq1, menuCreateReq2);
//
//        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
//
//        Menu menu1 = new Menu(1L, store, "메뉴 이름 1", 10000, "메뉴 설명 1",false);
//        Menu menu2 = new Menu(2L, store, "메뉴 이름 2", 12000, "메뉴 설명 2",false
//        when(menuRepository.saveAll(anyList())).thenReturn(Arrays.asList(menu1, menu2));
//
//        // when
//        menuService.createMenus(menuCreateReqList);
//
//        // then
//        verify(menuRepository).saveAll(anyList());
//    }
//
//    // Update menu test
//    @Test
//    void 메뉴_수정_성공() {
//        // given
//        Long menuId = 1L;
//        Menu existingMenu = new Menu(menuId, null, "Old Name", 10000, "Old Description");
//
//        MenuUpdateReq updateReq = new MenuUpdateReq("New Name", 12000, "New Description");
//
//        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));
//
//        // when
//        menuService.updateMenu(menuId, updateReq);
//
//        // then
//        assertEquals("New Name", existingMenu.getMenuName());
//        assertEquals(12000, existingMenu.getMenuPrice());
//        assertEquals("New Description", existingMenu.getDescription());
//        verify(menuRepository).save(existingMenu);
//    }
//
//    // Delete menu test
//    @Test
//    void 메뉴_삭제_성공() {
//        // given
//        Long menuId = 1L;
//        Menu existingMenu = new Menu(menuId, null, "Menu to be deleted", 10000, "Description");
//
//        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));
//
//        // when
//        menuService.deleteMenu(menuId);
//
//        // then
//        verify(menuRepository).save(existingMenu);
//        assertTrue(existingMenu.isDeleted());  // Check if menu is marked as deleted
//    }
//
//    // Update menu failure - menu not found
//    @Test
//    void 메뉴_수정_실패_메뉴_존재하지않음() {
//        // given
//        Long menuId = 1L;
//        MenuUpdateReq updateReq = new MenuUpdateReq("New Name", 12000, "New Description");
//
//        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());
//
//        // when & then
//        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
//            menuService.updateMenu(menuId, updateReq);
//        });
//        assertEquals(ErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
//    }
//
//    // Delete menu failure - menu not found
//    @Test
//    void 메뉴_삭제_실패_메뉴_존재하지않음() {
//        // given
//        Long menuId = 1L;
//
//        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());
//
//        // when & then
//        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
//            menuService.deleteMenu(menuId);
//        });
//        assertEquals(ErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
//    }
//
//    // Unauthorized user trying to create a menu
////    @Test
////    void 메뉴_생성_실패_권한없음() {
////        // given
////        Long storeId = 1L;
////        Store store = Store.builder()
////                .id(storeId)
////                .name("가게 이름")
////                .minOrderPrice(5000)
////                .starRate(0.0)
////                .isDeleted(false)
////                .build();
////
////        MenuCreateReq menuCreateReq = new MenuCreateReq(storeId, "메뉴 이름", 10000, "메뉴 설명");
////
////        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
////
////        // Simulate unauthorized user
////        doThrow(new CustomRuntimeException(ErrorCode.INVALID_USER_ROLE)).when(menuService).checkIfOwner();
////
////        // when & then
////        CustomRuntimeException exception = assertThrows(CustomRuntimeException.class, () -> {
////            menuService.createMenus(Arrays.asList(menuCreateReq));
////        });
////        assertEquals(ErrorCode.INVALID_USER_ROLE, exception.getErrorCode());
////    }
//}
