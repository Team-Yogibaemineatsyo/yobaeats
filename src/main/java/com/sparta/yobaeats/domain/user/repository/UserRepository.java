package com.sparta.yobaeats.domain.user.repository;

import com.sparta.yobaeats.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
