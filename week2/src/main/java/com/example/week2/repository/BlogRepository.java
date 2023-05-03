package com.example.week2.repository;

import com.example.week2.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByIdAndUserId(Long id, Long UserId);

    List<Blog> findAllByOrderByCreatedAtDesc();
}