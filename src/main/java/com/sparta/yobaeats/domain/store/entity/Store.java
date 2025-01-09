package com.sparta.yobaeats.domain.store.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double starRate;

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Store(
            Long id,
            String name,
            Integer minOrderPrice,
            Double starRate,
            LocalTime openAt,
            LocalTime closeAt,
            boolean isDeleted,
            User user,
            List<Menu> menus
    ) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.starRate = starRate;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.isDeleted = isDeleted;
        this.user = user;
        this.menus = menus;
    }

    public void update(
            String newName,
            LocalTime newOpenAt,
            LocalTime newCloseAt,
            Integer newMinOrderPrice
    ) {
        updateName(newName);
        updateOpenAt(newOpenAt);
        updateCloseAt(newCloseAt);
        updateMinOrderPrice(newMinOrderPrice);
    }

    public void delete() {
        this.isDeleted = true;
    }

    private void updateName(String newName) {
        if (newName != null) {
            this.name = newName;
        }
    }

    private void updateOpenAt(LocalTime newOpenAt) {
        if (newOpenAt != null) {
            this.openAt = newOpenAt;
        }
    }

    private void updateCloseAt(LocalTime newCloseAt) {
        if (newCloseAt != null) {
            this.closeAt = newCloseAt;
        }
    }

    private void updateMinOrderPrice(Integer newMinOrderPrice) {
        if (newMinOrderPrice != null) {
            this.minOrderPrice = newMinOrderPrice;
        }
    }
}

