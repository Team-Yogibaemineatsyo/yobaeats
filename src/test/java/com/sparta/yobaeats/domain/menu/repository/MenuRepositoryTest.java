package com.sparta.yobaeats.domain.menu.repository;

import com.sparta.yobaeats.domain.menu.entity.Menu;
import com.sparta.yobaeats.domain.menu.repository.MenuRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.repository.StoreRepository;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 메뉴_저장_성공() {
        // given
        // 임의의 User 객체 생성
        User user = User.builder()
                .email("사용자 이메일")
                .password("사용자 비밀번호")
                .nickName("사용자 이름")
                .role(UserRole.USER) // UserRole은 적절한 enum 값으로 설정
                .build();

        // 임의의 Store 객체 생성
        Store store = Store.builder()
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0) // 기본값으로 0.0 설정
                .openAt(LocalTime.of(10, 0))
                .closeAt(LocalTime.of(22, 0))
                .isDeleted(false) // 기본값으로 false 설정
                .user(user)
                .build();

        storeRepository.save(store); // Store를 저장하여 ID가 생성되게 함

        final String menuName = "메뉴 이름";
        final Integer menuPrice = 10000;
        final String description = "메뉴 설명";

        // 임의의 Menu 객체 생성
        Menu menu = Menu.builder()
                .store(store)
                .menuName(menuName)
                .menuPrice(menuPrice)
                .description(description)
                .build();

        // when
        Menu actualResult = menuRepository.save(menu);

        // then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getMenuName()).isEqualTo(menuName);
        assertThat(actualResult.getMenuPrice()).isEqualTo(menuPrice);
        assertThat(actualResult.getDescription()).isEqualTo(description);
        assertThat(actualResult.getStore()).isEqualTo(store);
    }

    @Test
    void 메뉴_삭제_성공() {
        // given
        // 임의의 User 객체 생성
        User user = User.builder()
                .email("사용자 이메일")
                .password("사용자 비밀번호")
                .nickName("사용자 이름")
                .role(UserRole.USER) // UserRole은 적절한 enum 값으로 설정
                .build();

        // 임의의 Store 객체 생성
        Store store = Store.builder()
                .name("가게 이름")
                .minOrderPrice(5000)
                .starRate(0.0) // 기본값으로 0.0 설정
                .openAt(LocalTime.of(10, 0))
                .closeAt(LocalTime.of(22, 0))
                .isDeleted(false) // 기본값으로 false 설정
                .user(user)
                .build();

        storeRepository.save(store); // Store 저장

        // 임의의 Menu 객체 생성
        Menu menu = Menu.builder()
                .store(store)
                .menuName("메뉴 이름")
                .menuPrice(10000)
                .description("메뉴 설명")
                .build();
        menuRepository.save(menu); // Menu 저장

        // when
        menuRepository.delete(menu); // Menu 삭제

        // then
        assertThat(menuRepository.findById(menu.getId())).isEmpty(); // Menu가 존재하지 않음을 확인
    }
}
