package com.example.week2.dto;

import com.example.week2.entity.Blog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@NoArgsConstructor
@Configuration
public class BlogResponseDto extends ResponseDto {  // 응답용 Dto 생성
    private BlogToDto blogOne;

    public BlogResponseDto(StatusEnum status, Blog blog) {
        super(status);
        this.blogOne = new BlogToDto(blog);
    }

    public BlogResponseDto(StatusEnum status, Blog blog, List<CommentToDto> commentList, boolean blogLikeCheck) {
        super(status);
        this.blogOne = new BlogToDto(blog, commentList, blogLikeCheck);
    }

}