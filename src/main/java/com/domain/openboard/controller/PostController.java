package com.domain.openboard.controller;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.PostListResponseDto;
import com.domain.openboard.dto.PostRequestDto;
import com.domain.openboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    // 게시글 목록 조회 API
    @GetMapping("/posts")
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {
        List<PostListResponseDto> postListDto = postService.findAll()
                .stream()                               // 컬렉션(List)를 Stream 객체로 변환
                .map(PostListResponseDto::new)          // 각 Post 객체를 PostListResponseDto로 변환
                .toList();                              // 변환된 요소들을 새로운 리스트(List<PostListResponseDto>)로 수집
                                                        // List<Post> -> Stream -> PostListResponseDto -> List<PostListResponseDto>
        return ResponseEntity.ok().body(postListDto);
    }
}
