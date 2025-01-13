package com.sparta.yobaeats.domain.reply.repository;

import com.sparta.yobaeats.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByIdAndIsDeletedFalse(Long replyId);

    @Query("SELECT r FROM Reply r " +
            "WHERE r.review.store.user.id = :userId " +
            "AND r.isDeleted = false " +
            "ORDER BY r.updatedAt DESC")
    List<Reply> findAllByUserId(@Param("userId") Long userId);
}