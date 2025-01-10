package com.sparta.yobaeats.domain.review.repository;

import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.review.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryQuerydsl {

    boolean existsByOrder(Order order);

    Optional<Review> findByIdAndIsDeletedFalse(Long reviewId);
}
