package com.sparta.yobaeats.domain.review.controller;

import com.sparta.yobaeats.domain.review.dto.request.ReviewReq;
import com.sparta.yobaeats.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(
        @Valid @RequestBody ReviewReq req
        // @AuthenticationPrincipal UserDetails userDetails
    ) {
        reviewService.createReview(req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
