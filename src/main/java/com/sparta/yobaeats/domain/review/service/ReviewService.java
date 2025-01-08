package com.sparta.yobaeats.domain.review.service;

import com.sparta.yobaeats.domain.review.dto.request.ReviewReq;
import com.sparta.yobaeats.domain.review.repository.ReviewRepository;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final StoreService storeService;
    //private final OrderService orderService;

    public void createReview(ReviewReq req) {
        //토큰에서 userId 받아와서 User 객체 생성
        /*User user = userService.findUserById(tokenId);
        Order order = orderService.findOrderById(req.orderId());
        Store store = order.getStore();
        // 사용자 검증
        if(!user.getId().equals(order.getUser().id)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        // 주문 상태 검증
        if(order.getStatus() != OrderStatus.DONE) {
            throw new InvalidOrderStatusException(ErrorCode.INVALID_ORDER_STATUS);
        }
        // 리뷰 중복 작성 검증
        if(reviewRepository.existsByOrder(order)) {
            throw new DuplicateReviewException(ErrorCode.DUPLICATE_REVIEW);
        }

        Review review = req.to(user, store, order);
        reviewRepository.save(review);
         */
    }
}
