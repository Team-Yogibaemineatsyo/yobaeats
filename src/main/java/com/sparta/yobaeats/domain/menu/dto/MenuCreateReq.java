package com.sparta.yobaeats.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateReq {
    private Long storeId;
    private String menuName;
    private Integer menuPrice;
    private String description;
}


