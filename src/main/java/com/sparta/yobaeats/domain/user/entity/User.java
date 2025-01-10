package com.sparta.yobaeats.domain.user.entity;

import com.sparta.yobaeats.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column
    @ColumnDefault("false")
    private boolean isDeleted;

    @Builder
    public User(Long id, String email, String password, String nickName, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.role = role;
    }

    public User(Long id, UserRole role) {
        this.id = id;
        this.role = role;
    }

    public void update(String password, String nickName) {
      updatePassword(password);
      updateNickName(nickName);
    }

    private void updatePassword(String newPassword) {
        if (newPassword != null) {
            this.password = newPassword;
        }
    }

    private  void updateNickName(String newNickName) {
        if (newNickName != null) {
            this.nickName = newNickName;
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
