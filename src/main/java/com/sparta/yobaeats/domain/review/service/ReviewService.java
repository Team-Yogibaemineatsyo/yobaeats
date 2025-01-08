package com.sparta.yobaeats.domain.review.service;

import com.sparta.yobaeats.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;
}
