package com.sparta.yobaeats.domain.reply.controller;

import com.sparta.yobaeats.domain.reply.dto.request.ReplyCreateReq;
import com.sparta.yobaeats.domain.reply.dto.request.ReplyUpdateReq;
import com.sparta.yobaeats.domain.reply.dto.response.ReplyReadListRes;
import com.sparta.yobaeats.domain.reply.dto.response.ReplyReadRes;
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
        Long replyId = replyService.createReply(replyContentReq, userDetails.getId());
        URI uri = UriBuilderUtil.create("/api/replies/{replyId}", replyId);

        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyReadRes> readReply(
            @PathVariable Long replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(replyService.readReply(replyId, userDetails.getId()));
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<ReplyReadListRes> readReplies(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReplyReadListRes replyReadListRes = new ReplyReadListRes(
                replyService.readReplies(userDetails.getId())
        );
        return ResponseEntity.ok(replyReadListRes);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{replyId}")
    public ResponseEntity<Void> updateReply(
            @PathVariable Long replyId,
            @RequestBody @Valid ReplyUpdateReq replyUpdateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        replyService.updateReply(replyId, userDetails.getId(), replyUpdateReq);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        replyService.deleteReply(replyId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
