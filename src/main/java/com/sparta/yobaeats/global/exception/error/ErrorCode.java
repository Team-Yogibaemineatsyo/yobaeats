package com.sparta.yobaeats.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 메뉴, 주문 관련 익셉션
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),

    // store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    STORE_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 폐업 처리된 가게입니다."),
    STORE_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "가게는 최대 3개까지만 운영할 수 있습니다"),

    // 유저 관련 익셉션
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    USER_DELETED(HttpStatus.GONE, "탈퇴한 유저입니다."),
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    // 리뷰 관련 익셉션
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "배달 완료 상태가 아닙니다."),
    INVALID_STAR_RANGE(HttpStatus.BAD_REQUEST, "별점 범위가 유효하지 않습니다."),
    STAR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 별점의 가게가 존재하지 않습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT,"이미 리뷰가 작성된 주문입니다.");

    private final HttpStatus status;
    private final String message;
}
