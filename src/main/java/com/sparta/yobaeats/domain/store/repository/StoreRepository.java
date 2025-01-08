package com.sparta.yobaeats.domain.store.repository;

import com.sparta.yobaeats.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByName(String storeName);
    Long countByUserIdAndIsDeletedFalse(Long userId);
}
