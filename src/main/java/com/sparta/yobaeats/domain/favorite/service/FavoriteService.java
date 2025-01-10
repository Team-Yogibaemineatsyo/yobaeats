package com.sparta.yobaeats.domain.favorite.service;

import com.sparta.yobaeats.domain.favorite.dto.request.FavoriteCreateReq;
import com.sparta.yobaeats.domain.favorite.dto.response.FavoriteReadRes;
import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import com.sparta.yobaeats.domain.favorite.repository.FavoriteRepository;
import com.sparta.yobaeats.domain.review.repository.ReviewRepository;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final StoreService storeService;
    private final ReviewRepository reviewRepository;

    public Long addFavorite(Long userId, FavoriteCreateReq favoriteCreateReq) {
        if (favoriteRepository.existsByUserIdAndStoreId(userId, favoriteCreateReq.storeId())) {
            throw new ConflictException(ErrorCode.DUPLICATED_FAVORITE);
        }

        storeService.findStoreById(favoriteCreateReq.storeId());

        favoriteRepository.save(favoriteCreateReq.to(userId));

        return favoriteCreateReq.storeId();
    }

    public List<FavoriteReadRes> readFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        if (favorites.isEmpty()) {
            throw new NotFoundException(ErrorCode.FAVORITE_NOT_FOUND);
        }
        return favorites.stream()
            .map(FavoriteReadRes::from)
            .toList();
    }

    public FavoriteReadRes readFavorite(Long userId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndUserId(favoriteId, userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FAVORITE_NOT_FOUND));

        storeService.findStoreById(favorite.getStoreId());

        return FavoriteReadRes.from(favorite);
    }


    public void deleteFavorite(Long userId, Long favoriteId) {

    }
}
