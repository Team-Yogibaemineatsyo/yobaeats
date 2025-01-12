package com.sparta.yobaeats.domain.reply.service;

import com.sparta.yobaeats.domain.reply.dto.request.ReplyCreateReq;
import com.sparta.yobaeats.domain.reply.dto.response.ReplyReadRes;
import com.sparta.yobaeats.domain.reply.entity.Reply;
import com.sparta.yobaeats.domain.reply.repository.ReplyRepository;
import com.sparta.yobaeats.domain.review.entity.Review;
import com.sparta.yobaeats.domain.review.service.ReviewService;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final ReviewService reviewService;

    @Transactional
    public Long createReply(ReplyCreateReq replyContentReq, Long userId) {
        // 리뷰 존재 확인
        Review review = reviewService.findReviewById(replyContentReq.reviewId());
        // 중복 댓글 확인
        if (review.getReply() != null) {
            throw new ConflictException(ErrorCode.DUPLICATED_REPLY);
        }
        // 권한 확인
        if (!userId.equals(review.getStore().getUser().getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        Reply reply = replyContentReq.to(userService.findUserById(userId), review);
        replyRepository.save(reply);

        return reply.getId();
    }

    public ReplyReadRes readReply(Long replyId, Long userId) {
        // 댓글 존재 확인
        Reply reply = findReplyById(replyId);
        // 권한 확인
        if (!userId.equals(reply.getUser().getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        return ReplyReadRes.from(reply);
    }

    public List<ReplyReadRes> readReplies(Long userId) {
        List<Reply> replies = replyRepository.findAllByUserIdOrderByUpdatedAtDesc(userId);

        return replies.stream()
                .map(ReplyReadRes::from)
                .toList();
    }

    public Reply findReplyById(Long replyId) {
        return replyRepository.findByIdAndIsDeletedFalse(replyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REPLY_NOT_FOUND));
    }
}
