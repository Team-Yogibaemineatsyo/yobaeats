package com.sparta.yobaeats.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.yobaeats.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sparta.yobaeats.domain.store.entity.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryQuerydslImpl implements StoreRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Store> findAllActiveByAndNameOrElseAll(String storeName) {
        BooleanBuilder builder = new BooleanBuilder();

        if (storeName != null) {
            builder.and(store.name.contains(storeName));
        }

        return queryFactory.selectFrom(store)
                .where(store.isDeleted.eq(false), builder)
                .fetch();
    }
}
