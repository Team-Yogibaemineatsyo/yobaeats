package com.sparta.yobaeats.domain.order.repository;

import com.sparta.yobaeats.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
