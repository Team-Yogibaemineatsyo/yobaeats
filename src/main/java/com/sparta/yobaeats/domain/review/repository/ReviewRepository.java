package com.sparta.yobaeats.domain.review.repository;

import com.sparta.yobaeats.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByStoreIdOrderByUpdatedAtDesc(Long storeId);

    List<Review> findByStarBetween(int startStar, int endStar);

    List<Review> findByStar(int startStar);
}
