package com.sparta.yobaeats.domain.menu.repository;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreIdAndIsDeletedFalse(Long storeId);
}
