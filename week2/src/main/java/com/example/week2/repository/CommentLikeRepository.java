package com.example.week2.repository;

import com.example.week2.entity.Comment;
import com.example.week2.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    void deleteByCommentIdAndUserId(Long commentId, Long userId);
}
