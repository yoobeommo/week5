package com.example.week2.dto;

import com.example.week2.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class CommentResponseDto extends ResponseDto {

    CommentToDto commentOne;


    public CommentResponseDto(StatusEnum status, Comment comment, boolean commentLikeCheck) {
        super(status);
        this.commentOne = new CommentToDto(comment, commentLikeCheck);
    }
}