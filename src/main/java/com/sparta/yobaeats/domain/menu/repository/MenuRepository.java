package com.sparta.yobaeats.domain.menu.repository;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * 특정 가게의 삭제되지 않은 메뉴를 조회합니다.
     *
     * @param menuId 메뉴의 ID
     * @return 해당 가게의 삭제되지 않은 메뉴
     */
    Optional<Menu> findByIdAndIsDeletedFalse(Long menuId);
}
