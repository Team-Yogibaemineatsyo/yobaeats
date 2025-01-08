package com.sparta.yobaeats.domain.user.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import com.sparta.yobaeats.domain.user.dto.UserRes;
import com.sparta.yobaeats.domain.user.dto.UserUpdateReq;
import com.sparta.yobaeats.domain.user.enums.UserRole;
import com.sparta.yobaeats.domain.user.exception.UserDeletedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
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
    public User(Long id, String email, String password, String nickName, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.role = role;
    }

    public UserRes from() {
        return new UserRes(
            this.email = email,
            this.nickName = nickName
        );
    }

    public void isDeletedUser() {
        if (this.isDeleted) {
            throw new UserDeletedException(ErrorCode.USER_DELETED);
        }
    }

    public void updateUser(UserUpdateReq req) {
        if (req.email() != null && !req.email().isBlank()) {
            this.email = req.email();
        }

        if (req.password() != null && !req.password().isBlank()) {
            this.password = req.password();
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
