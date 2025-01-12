package com.sparta.yobaeats.domain.cart.repository;

import com.sparta.yobaeats.domain.cart.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRedisRepository extends CrudRepository<Cart, Long> {
}
