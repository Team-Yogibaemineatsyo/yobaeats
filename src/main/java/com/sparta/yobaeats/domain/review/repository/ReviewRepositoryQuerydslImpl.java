package com.sparta.yobaeats.domain.review.repository;

import static com.sparta.yobaeats.domain.review.entity.QReview.review;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.yobaeats.domain.review.entity.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryQuerydslImpl implements ReviewRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findReviewsByStoreIdAndStar(
        Long storeId,
        Integer startStar,
        Integer endStar
    ) {
        return queryFactory
            .selectFrom(review)
            .where(
                review.store.id.eq(storeId),
                review.isDeleted.eq(false),
                starCondition(startStar, endStar)
            )
            .orderBy(review.updatedAt.desc())
            .fetch();
    }

    public BooleanExpression starCondition(Integer startStar, Integer endStar) {
        // 별점 둘 다 null
        if (startStar == null && endStar == null) {
            return null;
        }
        // 별점 둘 중 하나만 null
        if (startStar == null) {
            return review.star.eq(endStar);
        }
        if (endStar == null) {
            return review.star.eq(startStar);
        }
        // 별점이 같은 경우
        if (startStar.equals(endStar)) {
            return review.star.eq(startStar);
        }

        return review.star.between(startStar, endStar);
    }
}
