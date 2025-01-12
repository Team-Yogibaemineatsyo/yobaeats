package com.sparta.yobaeats.domain.store.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuantityType {
    INCREASE(1),
    DECREASE(-1),
    REMOVE(0);

    private final int value;
}
