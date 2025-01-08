package com.sparta.yobaeats.domain.review.repository;

import com.sparta.yobaeats.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
