package com.sparta.yobaeats.domain.store.repository;

import com.sparta.yobaeats.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryQuerydsl {

    Long countByUserIdAndIsDeletedFalse(Long userId);

    @EntityGraph(attributePaths = {"menus"})
    Optional<Store> findByIdAndIsDeletedFalse(Long storeId);
}
