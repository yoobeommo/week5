package com.example.week2.service;

import com.example.week2.dto.*;
import com.example.week2.entity.*;
import com.example.week2.exception.CustomException;
import com.example.week2.jwt.JwtUtil;
import com.example.week2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import java.util.List;
import java.util.Optional;


import static com.example.week2.exception.ErrorCode.AUTHORIZATION;
import static com.example.week2.exception.ErrorCode.NOT_FOUND_BOARD;

@Service
@RequiredArgsConstructor

public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final BlogLikeRepository blogLikeRepository;
    private final CommentLikeRepository commentLikeRepository;


    public BlogResponseDto createBlog(BlogRequestDto requestDto,  @AuthenticationPrincipal User user) {

        Blog blog = blogRepository.saveAndFlush(new Blog(requestDto, user));
        return new BlogResponseDto(StatusEnum.OK, blog);
    }

    @Transactional(readOnly = true)
    public ResponseDto getBlogs(User user) {
        BlogListResponseDto blogListResponseDto = new BlogListResponseDto(StatusEnum.OK);
        List<Blog> blogList = blogRepository.findAllByOrderByCreatedAtDesc();
        for (Blog blog : blogList) {
            List<CommentToDto> commentList = new ArrayList<>();
            for (Comment comment : blog.getComments()) {
                commentList.add(new CommentToDto(comment, checkCommentLike(comment.getId(), user)));
            }
            blogListResponseDto.add(new BlogToDto(
                    blog,
                    commentList,
                    // 해당 회원의 해당 게시글 좋아요 여부
                    (checkBlogLike(blog.getId(), user))));
        }
        return blogListResponseDto;
    }

    @Transactional(readOnly = true)
    public ResponseDto getBlogsOne(Long id, User user) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );
        List<CommentToDto> commentList = new ArrayList<>();
        for (Comment comment : blog.getComments()) {
            commentList.add(new CommentToDto(comment, checkCommentLike(comment.getId(), user)));
        }
        return new BlogResponseDto(
                StatusEnum.OK,
                blog,
                commentList,
                // 해당 회원의 해당 게시글 좋아요 여부
                (checkBlogLike(blog.getId(), user)));
    }

    public ResponseDto updateBlog(Long id, BlogRequestDto requestDto,  @AuthenticationPrincipal User user) {

        UserRoleEnum userRoleEnum = user.getRole();                     // 사용자의 User Type(User, Admin) 값 받아오기
        Blog blog;                                                      // Blog Entity 연결

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 게시글 id와 일치하는 DB 조회
            blog = blogRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BOARD)
            );
        } else {
            // 입력 받은 게시글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }

        blog.update(requestDto);

        List<CommentToDto> commentList = new ArrayList<>();
        for (Comment comment : blog.getComments()) {
            commentList.add(new CommentToDto(comment, checkCommentLike(comment.getId(), user)));
        }

        return new BlogResponseDto(StatusEnum.OK, blog, commentList, (checkBlogLike(blog.getId(), user)));
    }

    // Delete DB Function
    @Transactional
    public ResponseDto deleteBlog( Long id, @AuthenticationPrincipal User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Blog blog;

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 게시글 id와 일치하는 DB 조회
            blog = blogRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BOARD)
            );

        } else {
            // 입력 받은 게시글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }

        blogRepository.deleteById(id);

        return new ResponseDto(StatusEnum.OK);
    }


    @Transactional(readOnly = true)
    public boolean checkBlogLike(Long blogId, User user) {
        Optional<BlogLike> blogLike = blogLikeRepository.findByBlogIdAndUserId(blogId, user.getId());
        return blogLike.isPresent();
    }
    @Transactional(readOnly = true)
    public boolean checkCommentLike(Long commentId, User user) {
        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, user.getId());
        return commentLike.isPresent();
    }
    @Transactional
    public ResponseDto saveBlogLike(Long blogId, User user) {
        // 입력 받은 게시글 id와 일치하는 DB 조회
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 해당 회원의 좋아요 여부를 확인하고 비어있으면 좋아요, 아니면 좋아요 취소
        if (!checkBlogLike(blogId, user)) {
            blogLikeRepository.saveAndFlush(new BlogLike(blog, user));
            return new ResponseDto(StatusEnum.PLUS_LIKE);
        } else {
            blogLikeRepository.deleteByBlogIdAndUserId(blogId, user.getId());
            return new ResponseDto(StatusEnum.MINUS_LIKE);
        }
    }
}