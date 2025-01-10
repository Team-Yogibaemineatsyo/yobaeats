package com.sparta.yobaeats.domain.favorite.controller;

import com.sparta.yobaeats.domain.favorite.dto.request.FavoriteCreateReq;
import com.sparta.yobaeats.domain.favorite.dto.response.FavoriteReadListRes;
import com.sparta.yobaeats.domain.favorite.dto.response.FavoriteReadRes;
import com.sparta.yobaeats.domain.favorite.service.FavoriteService;
import com.sparta.yobaeats.domain.user.service.UserService;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Long> createFavorite(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody FavoriteCreateReq favoriteCreateReq
    ) {
        Long userId = userDetails.getId();
        Long storeId = favoriteService.addFavorite(userId, favoriteCreateReq);
        URI uri = UriBuilderUtil.create("/api/stores/{storeId}", storeId);

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<FavoriteReadListRes> readFavorites(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        FavoriteReadListRes favoriteReadListRes =
            new FavoriteReadListRes(favoriteService.readFavorites(userId));

        return ResponseEntity.ok(favoriteReadListRes);
    }

    @GetMapping("/{favoriteId}")
    public ResponseEntity<FavoriteReadRes> readFavorite(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long favoriteId
    ) {
        Long userId = userDetails.getId();

        return ResponseEntity.ok(favoriteService.readFavorite(userId, favoriteId));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long favoriteId
    ) {
        Long userId = userDetails.getId();
        favoriteService.deleteFavorite(userId, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
