package com.sparta.yobaeats.domain.menu.entity;

import com.sparta.yobaeats.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Store store;

    @Column(name = "menu_name", nullable = false, length = 30)
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Menu(Long id, Store store, String menuName, Integer menuPrice, String description) {
        this.id = id;
        this.store = store;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.description = description;
    }


    public static Menu update(Menu existingMenu, String menuName, Integer menuPrice, String description) {
        return Menu.builder()
                .id(existingMenu.getId())
                .store(existingMenu.getStore()) // 기존 store 객체를 그대로 사용
                .menuName(menuName)
                .menuPrice(menuPrice)
                .description(description)
                .build();
    }

    // 메뉴 삭제 상태를 변경하는 메서드
    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
