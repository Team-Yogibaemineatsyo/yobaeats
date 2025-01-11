package com.sparta.yobaeats.domain.menu.repository;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * 특정 가게의 삭제되지 않은 메뉴 목록을 조회합니다.
     *
     * @param storeId 메뉴가 속한 가게의 ID
     * @return 해당 가게의 삭제되지 않은 메뉴 리스트
     */
    List<Menu> findByStoreIdAndIsDeletedFalse(Long storeId);
}
