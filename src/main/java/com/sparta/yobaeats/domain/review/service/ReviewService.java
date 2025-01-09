package com.sparta.yobaeats.domain.review.service;

import com.sparta.yobaeats.domain.order.entity.Order;
import com.sparta.yobaeats.domain.order.entity.Order.Status;
import com.sparta.yobaeats.domain.order.service.OrderService;
import com.sparta.yobaeats.domain.review.dto.request.ReviewReq;
import com.sparta.yobaeats.domain.review.dto.response.ReviewRes;
import com.sparta.yobaeats.domain.review.entity.Review;
import com.sparta.yobaeats.domain.review.exception.DuplicateReviewException;
import com.sparta.yobaeats.domain.review.exception.InvalidOrderStatusException;
import com.sparta.yobaeats.domain.review.exception.InvalidStarRangeException;
import com.sparta.yobaeats.domain.review.repository.ReviewRepository;
import com.sparta.yobaeats.domain.store.entity.Store;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.entity.User;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final StoreService storeService;
    private final OrderService orderService;

    @Transactional
    public void createReview(ReviewReq req) {
        //토큰에서 userId 받아와서 User 객체 생성
        Long tokenId = 1L; // 나중에 삭제...임시

        User user = userService.findUserById(tokenId);
        Order order = orderService.findOrderById(req.orderId());
        Store store = order.getStore();
        // 사용자 검증
        if(!user.getId().equals(order.getUser().getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        // 주문 상태 검증
        if(order.getStatus() != Status.DELIVERED) {
            throw new InvalidOrderStatusException(ErrorCode.INVALID_ORDER_STATUS);
        }
        // 리뷰 중복 작성 검증
        if(reviewRepository.existsByOrder(order)) {
            throw new DuplicateReviewException(ErrorCode.DUPLICATE_REVIEW);
        }

        Review review = req.to(user, order, store);

    }

    public List<ReviewRes> findByStoreId(Long storeId) {
        List<Review> reviewList = reviewRepository
            .findAllByStoreIdOrderByUpdatedAtDesc(storeId);

        return reviewList.stream()
            .map(ReviewRes::from)
            .toList();
    }

    public List<ReviewRes> findByStar(int startStar, int endStar) {
        if (startStar > endStar) {
            throw new InvalidStarRangeException(ErrorCode.INVALID_STAR_RANGE);
        }

        List<Review> reviewList = startStar == endStar ?
            reviewRepository.findByStar(startStar) :
            reviewRepository.findByStarBetween(startStar, endStar);

        return reviewList.stream()
            .map(ReviewRes::from)
            .toList();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));
        Long userId = review.getUser().getId();

        // 뒤의 userId 추후에 토큰아이디로 변경
        userService.validateUser(userId, userId);

        review.softDelete();
    }
}
