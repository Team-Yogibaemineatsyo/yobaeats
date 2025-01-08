package com.sparta.yobaeats.domain.store.repository;

import com.sparta.yobaeats.domain.store.entity.Store;

import java.util.List;

public interface StoreRepositoryQuerydsl {

    List<Store> findAllActiveByAndNameOrElseAll(String StoreName);
}
