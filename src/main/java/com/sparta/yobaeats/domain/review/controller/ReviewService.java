package com.sparta.yobaeats.domain.review.controller;

import com.sparta.yobaeats.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewService reviewService;
}
