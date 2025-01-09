package com.sparta.yobaeats.domain.menu.dto;

/**
 * 메뉴 수정 요청 데이터를 담는 DTO 클래스
 *
 * 이 클래스는 메뉴 수정 API에서 클라이언트가 전송하는 요청 데이터를 나타냅니다.
 * 필요한 필드는 모두 불변(immutable)하도록 record로 정의되었습니다.
 */
public record MenuUpdateReq(
        String menuName,
        Integer menuPrice,
        String description
) {}
