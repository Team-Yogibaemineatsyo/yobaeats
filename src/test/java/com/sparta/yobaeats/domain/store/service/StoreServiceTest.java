package com.sparta.yobaeats.domain.store.service;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.store.dto.request.StoreCreateReq;
import com.sparta.yobaeats.domain.store.dto.request.StoreUpdateReq;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadDetailRes;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadSimpleRes;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;
    @Mock
    private UserService userService;
    @Mock
    private StoreRepository storeRepository;

    @Test
    void 가게_등록_성공() {
        // given
        Long userId = 1L;
        User user = User.builder().id(userId).role(UserRole.ROLE_OWNER).build();
        StoreCreateReq request = new StoreCreateReq("테스트 치킨", LocalTime.of(12, 0), LocalTime.of(22, 0), 15000);
        Store store = Store.builder()
                .id(1L)
                .name(request.storeName())
                .openAt(request.openAt())
                .closeAt(request.closeAt())
                .minOrderPrice(request.minOrderPrice())
                .build();

        given(storeRepository.countByUserIdAndIsDeletedFalse(userId)).willReturn(0L);
        given(userService.findUserById(userId)).willReturn(user);
        given(storeRepository.save(any(Store.class))).willReturn(store);

        // when
        Long storeId = storeService.createStore(request, user.getId());

        // then
        assertNotNull(storeId);
        assertEquals(store.getId(), storeId);

        verify(storeRepository).countByUserIdAndIsDeletedFalse(userId);
        verify(userService).findUserById(userId);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    void 가게_등록_실패_가게_최대_수_초과() {
        // given
        Long userId = 1L;
        StoreCreateReq request = new StoreCreateReq("테스트 치킨", LocalTime.parse("12:00"), LocalTime.parse("12:00"), 15000);

        given(storeRepository.countByUserIdAndIsDeletedFalse(userId)).willReturn(3L);

        // when
        ConflictException exception = assertThrows(ConflictException.class,
                () -> storeService.createStore(request, userId));

        // then
        assertEquals(HttpStatus.CONFLICT, exception.getErrorCode().getStatus());
        assertEquals("가게는 최대 3개까지만 운영할 수 있습니다", exception.getErrorCode().getMessage());

        verify(storeRepository).countByUserIdAndIsDeletedFalse(userId);
        verify(userService, never()).findUserById(userId);
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    void 가게_상세_조회_성공() {
        // given
        Long storeId = 1L;
        Store store = Store.builder()
                .id(storeId)
                .name("테스트 치킨")
                .openAt(LocalTime.of(12, 0))
                .closeAt(LocalTime.of(22, 0))
                .minOrderPrice(15000)
                .menus(List.of(Menu.builder()
                                        .id(1L)
                                        .menuName("치킨1")
                                        .menuPrice(12000)
                                        .description("맛있어")
                                        .build(),
                                Menu.builder()
                                        .id(2L)
                                        .menuName("치킨2")
                                        .menuPrice(16000)
                                        .description("맛없어")
                                        .build()
                        )
                )
                .build();
        given(storeRepository.findByIdAndIsDeletedFalse(storeId)).willReturn(Optional.of(store));

        // when
        StoreReadDetailRes result = storeService.readStore(storeId);

        // then
        assertEquals(storeId, result.storeId());
        assertEquals(store.getName(), result.storeName());
        assertEquals(store.getOpenAt(), result.openAt());
        assertEquals(store.getCloseAt(), result.closeAt());
        assertEquals(store.getMinOrderPrice(), result.minOrderPrice());

        assertEquals(1, result.menus().size());

        verify(storeRepository).findByIdAndIsDeletedFalse(storeId);
    }

    @Test
    void 가게_조회_실패_가게가_존재하지_않음() {
        // given
        Long storeId = 1L;
        given(storeRepository.findByIdAndIsDeletedFalse(storeId)).willReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> storeService.readStore(storeId));

        // then
        assertEquals(ErrorCode.STORE_NOT_FOUND.getMessage(), exception.getErrorCode().getMessage());

        verify(storeRepository).findByIdAndIsDeletedFalse(storeId);
    }

    @Test
    void 가게_단건_조회_성공() {
        // given
        String storeName = "치킨";
        Store store1 = Store.builder()
                .id(1L)
                .name("테스트 치킨1")
                .minOrderPrice(15000)
                .starRate(4.0)
                .isDeleted(false)
                .build();
        Store store2 = Store.builder()
                .id(2L)
                .name("테스트 치킨2")
                .minOrderPrice(20000)
                .starRate(4.5)
                .isDeleted(true)
                .build();
        List<Store> stores = List.of(store1, store2);

        given(storeRepository.findAllActiveByAndNameOrElseAll(storeName)).willReturn(stores);

        // when
        List<StoreReadSimpleRes> resultList = storeService.readStores(storeName);

        // then
        assertEquals(1, resultList.size());

        StoreReadSimpleRes result = resultList.get(0);
        assertEquals(store1.getId(), result.storeId());
        assertEquals(store1.getName(), result.storeName());
        assertEquals(store1.getMinOrderPrice(), result.minOrderPrice());
        assertEquals(store1.getStarRate(), result.starRate());

        verify(storeRepository).findAllActiveByAndNameOrElseAll(storeName);
    }

    @Test
    void 가게_리스트_이름_검색_조회_성공() {
        // given
        String storeName = null;
        Store store1 = Store.builder()
                .id(1L)
                .name("테스트 치킨1")
                .minOrderPrice(15000)
                .starRate(4.0)
                .build();
        List<Store> stores = List.of(store1);

        given(storeRepository.findAllActiveByAndNameOrElseAll(storeName)).willReturn(stores);

        // when
        List<StoreReadSimpleRes> resultList = storeService.readStores(storeName);

        // then
        assertEquals(1, resultList.size());

        StoreReadSimpleRes result = resultList.get(0);
        assertEquals(store1.getId(), result.storeId());
        assertEquals(store1.getName(), result.storeName());
        assertEquals(store1.getMinOrderPrice(), result.minOrderPrice());
        assertEquals(store1.getStarRate(), result.starRate());

        verify(storeRepository).findAllActiveByAndNameOrElseAll(storeName);
    }

    @Test
    void 가게_정보_수정_성공() {
        // given
        Long storeId = 1L;
        Long userId = 1L;
        Store store = Store.builder()
                .id(storeId)
                .name("기존 가게명")
                .openAt(LocalTime.of(10, 0))
                .closeAt(LocalTime.of(22, 0))
                .minOrderPrice(15000)
                .user(User.builder().id(userId).build())
                .build();
        StoreUpdateReq request = new StoreUpdateReq("수정된 가게명", LocalTime.of(11, 0), LocalTime.of(23, 0), 20000);

        given(storeRepository.findByIdAndIsDeletedFalse(storeId)).willReturn(Optional.of(store));

        // when
        storeService.updateStore(storeId, request, userId);

        // then
        assertEquals(request.storeName(), store.getName());
        assertEquals(request.openAt(), store.getOpenAt());
        assertEquals(request.closeAt(), store.getCloseAt());
        assertEquals(request.minOrderPrice(), store.getMinOrderPrice());

        verify(userService).validateUser(userId, userId);
        verify(storeRepository).findByIdAndIsDeletedFalse(storeId);
    }

    @Test
    void 가게_삭제_성공() {
        // given
        Long storeId = 1L;
        Long userId = 1L;
        Store store = Store.builder()
                .id(storeId)
                .user(User.builder().id(userId).build())
                .isDeleted(false)
                .build();

        given(storeRepository.findByIdAndIsDeletedFalse(storeId)).willReturn(Optional.of(store));

        // when
        storeService.deleteStore(storeId, userId);

        // then
        assertTrue(store.isDeleted());
        verify(userService).validateUser(userId, userId);
        verify(storeRepository).findByIdAndIsDeletedFalse(storeId);
    }

    @Test
    void 가게_삭제_실패() {
        // given
        Long storeId = 1L;
        Long userId = 1L;
        Store store = Store.builder()
                .id(storeId)
                .user(User.builder().id(userId).build())
                .isDeleted(true)
                .build();

        given(storeRepository.findByIdAndIsDeletedFalse(storeId)).willReturn(Optional.of(store));

        // when
        ConflictException exception = assertThrows(ConflictException.class,
                () -> storeService.deleteStore(storeId, userId));

        // then
        assertEquals(ErrorCode.STORE_ALREADY_DELETED.getMessage(), exception.getErrorCode().getMessage());
        verify(userService).validateUser(userId, userId);
        verify(storeRepository).findByIdAndIsDeletedFalse(storeId);
    }
}