package com.sparta.yobaeats.domain.favorite.service;

import com.sparta.yobaeats.domain.favorite.dto.request.FavoriteCreateReq;
import com.sparta.yobaeats.domain.favorite.dto.response.FavoriteReadRes;
import com.sparta.yobaeats.domain.favorite.entity.Favorite;
import com.sparta.yobaeats.domain.favorite.repository.FavoriteRepository;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.global.exception.ConflictException;
import com.sparta.yobaeats.global.exception.NotFoundException;
import com.sparta.yobaeats.global.exception.UnauthorizedException;
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
    private final StoreService storeService;

    @Transactional
    public Long addFavorite(Long userId, FavoriteCreateReq favoriteCreateReq) {
        if (favoriteRepository.existsByUserIdAndStoreId(userId, favoriteCreateReq.storeId())) {
            throw new ConflictException(ErrorCode.DUPLICATED_FAVORITE);
        }

        Long storeId = favoriteCreateReq.storeId();
        if (userId.equals(storeService.findStoreById(storeId).getUser().getId())) {
            throw new UnauthorizedException(ErrorCode.SELF_FAVORITE_NOT_ALLOWED);
        }

        favoriteRepository.save(favoriteCreateReq.to(userId));

        return storeId;
    }

    public List<FavoriteReadRes> readFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

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

    @Transactional
    public void deleteFavorite(Long userId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndUserId(favoriteId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
    }
}