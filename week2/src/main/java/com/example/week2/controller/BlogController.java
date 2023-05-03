package com.example.week2.controller;

import com.example.week2.Security.UserDetailsImpl;

import com.example.week2.dto.BlogRequestDto;

import com.example.week2.dto.ResponseDto;
import com.example.week2.service.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;



    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createBlog(@RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(blogService.createBlog(requestDto, userDetails.getUser()));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getBlogs(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok().body(blogService.getBlogs(userDetails.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getBlogsOne(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok().body(blogService.getBlogsOne(id, userDetails.getUser()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto> updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(blogService.updateBlog(id, requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteBlog(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(blogService.deleteBlog(id, userDetails.getUser()));
    }
    // 게시글 좋아요
    @PostMapping("/like/{boardId}")
    public ResponseEntity<ResponseDto> saveBlogLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(blogService.saveBlogLike(boardId, userDetails.getUser()));
    }
}