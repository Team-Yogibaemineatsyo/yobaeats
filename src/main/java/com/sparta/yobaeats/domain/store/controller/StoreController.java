package com.sparta.yobaeats.domain.store.controller;

import com.sparta.yobaeats.domain.store.dto.request.StoreCreateReq;
import com.sparta.yobaeats.domain.store.dto.response.StoreReadDetailRes;
import com.sparta.yobaeats.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // OWNER 권한 필요
    @PostMapping
    public ResponseEntity<Void> createStore(
            @RequestBody @Valid final StoreCreateReq request
    ) {
        // userId 필요
        final Long userId = 1L;

        final Long storeId = storeService.createStore(request, userId);

        final URI uri = UriComponentsBuilder.fromPath("/api/stores/{storeId}")
                .buildAndExpand(storeId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreReadDetailRes> readStore(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.readStore(storeId));
    }
}
