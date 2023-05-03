package com.example.week2.controller;

import com.example.week2.Security.UserDetailsImpl;
import com.example.week2.dto.CommentRequestDto;

import com.example.week2.dto.ResponseDto;
import com.example.week2.service.CommentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // insert Comment
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.createComment(id, requestDto, userDetails.getUser()));
    }

    // Comment Update
    @PutMapping("/{boardId}/{commentId}")
    public ResponseEntity<ResponseDto> updateComment( @PathVariable Long boardId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.updateComment(boardId, commentId, requestDto, userDetails.getUser()));
    }

    // Comment Delete
    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.deleteComment(boardId, commentId, userDetails.getUser()));
    }

    // 댓글 좋아요
    @PostMapping("/like/{commentId}")
    public ResponseEntity<ResponseDto> saveCommentLike(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.saveCommentLike(commentId, userDetails.getUser()));
    }
}
