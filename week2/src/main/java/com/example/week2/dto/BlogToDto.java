package com.example.week2.dto;

import com.example.week2.entity.Blog;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BlogToDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private int blogLikeCount;
    private boolean blogLikeCheck;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentToDto> commentList = new ArrayList<>();

    public BlogToDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.username = blog.getUsername();
        this.blogLikeCount = blog.getBlogLikeList().size();
        this.blogLikeCheck = false;
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
    }

    public BlogToDto(Blog blog,  List<CommentToDto> commentList, boolean blogLikeCheck) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.username = blog.getUsername();
        this.blogLikeCount = blog.getBlogLikeList().size();
        this.blogLikeCheck = blogLikeCheck;
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
        this.commentList = commentList;
    }
}
