package com.sparta.yobaeats.domain.store.service;

import com.sparta.yobaeats.domain.store.dto.request.StoreCreateReq;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadDetailRes;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadSimpleRes;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.domain.user.entity.User;
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

    private final StoreRepository storeRepository;

    public Long createStore(StoreCreateReq request, Long userId) {
        User user = User.builder().id(userId).build();

        Store store = storeRepository.save(request.toEntity(user));

        return store.getId();
    }

    public StoreReadDetailRes readStore(Long storeId) {
        Store store = findStoreById(storeId);

        return StoreReadDetailRes.from(store);
    }

    public List<StoreReadSimpleRes> readStores(String storeName) {
        List<Store> store = storeRepository.findAllByName(storeName);

        return store.stream()
                .map(StoreReadSimpleRes::from)
                .toList();
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }
}
