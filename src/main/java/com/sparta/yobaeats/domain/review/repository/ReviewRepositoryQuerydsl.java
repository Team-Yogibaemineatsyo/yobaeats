package com.sparta.yobaeats.domain.review.repository;

import com.sparta.yobaeats.domain.review.entity.Review;
import java.util.List;

public interface ReviewRepositoryQuerydsl {

    List<Review> findReviewsByStoreIdAndStar(Long storeId, Integer startStar, Integer endStar);
}
