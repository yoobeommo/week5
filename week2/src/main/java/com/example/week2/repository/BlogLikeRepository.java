package com.example.week2.repository;
import com.example.week2.entity.BlogLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogLikeRepository extends JpaRepository<BlogLike, Long> {
    Optional<BlogLike> findByBlogIdAndUserId(Long blogId, Long userId);

    void deleteByBlogIdAndUserId(Long blogId, Long userId);
}
