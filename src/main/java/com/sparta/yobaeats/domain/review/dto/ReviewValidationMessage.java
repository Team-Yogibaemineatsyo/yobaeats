package com.sparta.yobaeats.domain.review.dto;

public class ReviewValidationMessage {

    public static final String ORDER_ID_BLANK_MESSAGE = "OderId를 입력해 주세요.";
    public static final String STAR_BLANK_MESSAGE = "별점을 입력해 주세요.";
    public static final String STAR_MAX = "최대 별점은 5입니다.";
    public static final String STAR_MIN = "최소 별점은 1입니다.";
    public static final String REVIEW_BLANK_MESSAGE = "리뷰 내용을 입력해 주세요.";
    public static final String REVIEW_MESSAGE_RANGE = "리뷰 내용은 1자~10자 내로 입력해 주세요.";
}
