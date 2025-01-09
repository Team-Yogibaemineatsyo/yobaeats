package com.sparta.yobaeats.domain.store.controller;

import com.sparta.yobaeats.domain.auth.entity.UserDetailsCustom;
import com.sparta.yobaeats.domain.store.dto.request.StoreCreateReq;
import com.sparta.yobaeats.domain.store.dto.request.StoreUpdateReq;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadDetailRes;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadSimpleRes;
import com.sparta.yobaeats.domain.store.service.StoreService;
import com.sparta.yobaeats.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Void> createStore(
            @RequestBody @Valid StoreCreateReq request,
            @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        Long storeId = storeService.createStore(request, userDetails.getId());
        URI uri = UriBuilderUtil.create("/api/stores/{storeId}", storeId);

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreReadDetailRes> readStore(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.readStore(storeId));
    }

    @GetMapping
    public ResponseEntity<List<StoreReadSimpleRes>> readStores(
            @RequestParam(required = false) String storeName
    ) {
        return ResponseEntity.ok(storeService.readStores(storeName));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateReq request,
            @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        storeService.updateStore(storeId, request, userDetails.getId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable Long storeId,
            @AuthenticationPrincipal UserDetailsCustom userDetails
    ) {
        storeService.deleteStore(storeId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
