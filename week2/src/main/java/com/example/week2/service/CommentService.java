package com.example.week2.service;

import com.example.week2.dto.CommentRequestDto;
import com.example.week2.dto.CommentResponseDto;
import com.example.week2.dto.ResponseDto;
import com.example.week2.dto.StatusEnum;
import com.example.week2.entity.*;
import com.example.week2.exception.CustomException;
import com.example.week2.jwt.JwtUtil;
import com.example.week2.repository.BlogRepository;
import com.example.week2.repository.CommentLikeRepository;
import com.example.week2.repository.CommentRepository;
import com.example.week2.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Optional;

import static com.example.week2.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CommentLikeRepository commentLikeRepository;

    // Blog Comment Create Function
    @Transactional
    public ResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {

        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 요청 받은 DTO로 DB에 저장할 객체 만들기
        Comment comment = commentRepository.save(new Comment(requestDto, blog, user));

        return new CommentResponseDto(StatusEnum.OK, comment, false);
    }

    public ResponseDto updateComment(Long boardId, Long commentId, CommentRequestDto requestDto, User user) {

        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 사용자 권한 가져와서 ADMIN 이면 무조건 수정 가능, USER 면 본인이 작성한 댓글일 때만 수정 가능
        UserRoleEnum userRoleEnum = user.getRole();

        Comment comment;

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 댓글 id와 일치하는 DB 조회
            comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        } else {
            // 입력 받은 댓글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }

        // 요청 받은 DTO로 DB에 업데이트
        comment.update(requestDto);

        return new CommentResponseDto(StatusEnum.OK, comment, checkCommentLike(comment.getId(), user));
    }

    @Transactional(readOnly = true)
    public boolean checkCommentLike(Long commentId, User user) {
        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, user.getId());
        return commentLike.isPresent();
    }
    public ResponseDto deleteComment(Long blogId, Long commentId, User user) {

        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 사용자 권한 가져와서 ADMIN 이면 무조건 수정 가능, USER 면 본인이 작성한 댓글일 때만 수정 가능
        UserRoleEnum userRoleEnum = user.getRole();

        Comment comment;

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 댓글 id와 일치하는 DB 조회
            comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        } else {
            // 입력 받은 댓글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }

        // 해당 댓글 삭제
        commentRepository.deleteById(commentId);

        return new ResponseDto(StatusEnum.OK);
    }
    @Transactional
    public ResponseDto saveCommentLike(Long commentId, User user) {
        // 입력 받은 댓글 id와 일치하는 DB 조회
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(NOT_FOUND_COMMENT)
        );

        // 해당 회원의 좋아요 여부를 확인하고 비어있으면 좋아요, 아니면 좋아요 취소
        if (!checkCommentLike(commentId, user)) {
            commentLikeRepository.saveAndFlush(new CommentLike(comment, user));
            return new ResponseDto(StatusEnum.PLUS_LIKE);
        } else {
            commentLikeRepository.deleteByCommentIdAndUserId(commentId, user.getId());
            return new ResponseDto(StatusEnum.MINUS_LIKE);
        }
    }

}