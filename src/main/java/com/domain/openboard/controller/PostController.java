package com.domain.openboard.controller;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.PostRequestDto;
import com.domain.openboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api") // 전체 기본 경로
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 변환
public class PostController {

    private final PostService postService;

    // 게시글 작성 API
    @PostMapping("/posts")
    public ResponseEntity<Post> addPost(@RequestBody PostRequestDto dto){
        Post post = postService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(post);
    }
}
