package com.sparta.yobaeats.domain.store.dto;

public class StoreValidationMessage {

    public static final String NAME_BLANK_MESSAGE = "가게명을 입력해주세요.";
    public static final int NAME_MAX = 30;
    public static final String NAME_MAX_MESSAGE = "가게명은 30글자를 넘을 수 없습니다.";
    public static final String OPEN_AT_NULL_MESSAGE = "오픈 시간을 입력해주세요.";
    public static final String CLOSE_AT_NULL_MESSAGE = "마감 시간을 입력해주세요.";
    public static final String MIN_ORDER_PRICE_NULL_MESSAGE = "최소 주문 금액을 입력해주세요.";
}
