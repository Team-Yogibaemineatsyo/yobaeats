package com.sparta.yobaeats.domain.menu.service;

import com.sparta.yobaeats.domain.menu.dto.request.MenuCreateReq;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void 메뉴_여러개_생성_성공() {
        // given
        Long storeId = 1L;

        // Store 객체를 생성
        Store store = Store.builder()
                .id(storeId)
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0)
                .isDeleted(false)
                .build();

        // 메뉴 생성 요청 DTO 목록
        MenuCreateReq menuCreateReq1 = new MenuCreateReq(storeId, "메뉴 이름 1", 10000, "메뉴 설명 1");
        MenuCreateReq menuCreateReq2 = new MenuCreateReq(storeId, "메뉴 이름 2", 12000, "메뉴 설명 2");
        List<MenuCreateReq> menuCreateReqList = Arrays.asList(menuCreateReq1, menuCreateReq2);

        // Store 객체를 반환하도록 Mocking
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // Menu 객체를 Store 객체와 함께 반환하도록 Mocking
        Menu menu1 = new Menu(1L, store, "메뉴 이름 1", 10000, "메뉴 설명 1");
        Menu menu2 = new Menu(2L, store, "메뉴 이름 2", 12000, "메뉴 설명 2");

        // Mock saveAll method to return a list of menus
        when(menuRepository.saveAll(anyList())).thenReturn(Arrays.asList(menu1, menu2));

        // when
        menuService.createMenus(menuCreateReqList);  // 메뉴 생성 서비스 호출

        // then
        verify(menuRepository).saveAll(anyList());  // 메뉴들이 saveAll로 저장되었는지 확인
    }
}
