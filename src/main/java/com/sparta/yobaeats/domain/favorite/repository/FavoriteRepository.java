package com.sparta.yobaeats.domain.favorite.repository;

import com.sparta.yobaeats.domain.favorite.dto.response.FavoriteReadRes;
import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByIdAndUserId(Long favoriteId, Long userId);
}
