package com.sparta.yobaeats.domain.user.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Builder
    public User(String email, String password, String nickName, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.role = role;
    }
}
