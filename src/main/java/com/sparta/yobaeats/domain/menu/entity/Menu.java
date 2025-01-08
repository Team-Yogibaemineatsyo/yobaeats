package com.sparta.yobaeats.domain.menu.entity;

import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId; // Store ID로 참조(병합 후 삭제)

//    @ManyToOne (병합 후 주석풀기)
//    @JoinColumn(name = "store_id", nullable = false)
//    private Store store;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Menu(Long id, Long storeId, String menuName, Integer menuPrice, String description) {
        this.id = id;
        // this.store = store;(병합 후 주석풀기)
        this.storeId = storeId; // Store ID로 초기화(병합 후 삭제)
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.description = description;
    }

    public static Menu update(Menu existingMenu, String menuName, Integer menuPrice, String description) {
        return new Menu(
                existingMenu.getId(),
                existingMenu.getStoreId(),
                menuName,
                menuPrice,
                description
        );
    }

    // 메뉴 삭제 상태를 변경하는 메서드
    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
