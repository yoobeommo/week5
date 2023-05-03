package com.example.week2.dto;

import com.example.week2.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class CommentToDto {     // entity -> DTO

    private Long id;
    private String username;
    private String comment;
    private int commentLikeCount;
    private boolean commentLikeCheck;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public CommentToDto(Comment comment, boolean commentLikeCheck){
        this.id = comment.getId();// 댓글 Id
        this.username = comment.getUsername();
        this.comment = comment.getComment();      // 댓글 내용
        this.commentLikeCount = comment.getCommentLikeList().size();
        this.commentLikeCheck = commentLikeCheck;
        this.createdAt = comment.getCreatedAt();    // 댓글 생성시간
        this.modifiedAt = comment.getModifiedAt();  // 댓글 수정시간
    }
}