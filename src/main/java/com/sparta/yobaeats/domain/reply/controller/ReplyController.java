package com.sparta.yobaeats.domain.reply.controller;

import com.sparta.yobaeats.domain.reply.dto.request.ReplyCreateReq;
import com.sparta.yobaeats.domain.reply.service.ReplyService;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import com.sparta.yobaeats.global.util.UriBuilderUtil;

import java.net.URI;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<Void> createReply(
            @RequestBody @Valid ReplyCreateReq replyContentReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long reviewId = replyService.createReply(replyContentReq, userDetails.getId());
        URI uri = UriBuilderUtil.create("/api/stores/{storeId}", reviewId);

        return ResponseEntity.created(uri).build();
    }
}
