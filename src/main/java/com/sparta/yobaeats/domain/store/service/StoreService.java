package com.sparta.yobaeats.domain.store.service;

import com.sparta.yobaeats.domain.store.dto.request.StoreCreateReq;
import com.sparta.yobaeats.domain.store.dto.request.StoreUpdateReq;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadDetailRes;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadSimpleRes;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final UserService userService;
    private final StoreRepository storeRepository;
    private static final int MAX_OWNED_STORES = 3;

    @Transactional
    public Long createStore(StoreCreateReq request, Long userId) {
        if (MAX_OWNED_STORES <= storeRepository.countByUserIdAndIsDeletedFalse(userId)) {
            throw new ConflictException(ErrorCode.STORE_LIMIT_EXCEEDED);
        }

        User user = userService.findUserById(userId);
        Store store = storeRepository.save(request.toEntity(user));

        return store.getId();
    }

    public StoreReadDetailRes readStore(Long storeId) {
        Store store = findStoreById(storeId);

        return StoreReadDetailRes.from(store);
    }

    public List<StoreReadSimpleRes> readStores(String storeName) {
        List<Store> store = storeRepository.findAllActiveByAndNameOrElseAll(storeName);

        return store.stream()
                .map(StoreReadSimpleRes::from)
                .toList();
    }

    @Transactional
    public void updateStore(Long userId, Long storeId, StoreUpdateReq request) {
        User user = userService.findUserById(userId);

        Store store = findStoreById(storeId);

        store.update(
                request.storeName(), request.openAt(),
                request.closeAt(), request.minOrderPrice()
        );
    }

    @Transactional
    public void deleteStore(Long userId, Long storeId) {
        User user = userService.findUserById(userId);

        Store store = findStoreById(storeId);

        store.delete();
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }
}
