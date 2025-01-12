package com.sparta.yobaeats.domain.reply.repository;

import com.sparta.yobaeats.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByIdAndIsDeletedFalse(Long replyId);

    List<Reply> findAllByUserIdAndIsDeletedFalseOrderByUpdatedAtDesc(Long userId);
}