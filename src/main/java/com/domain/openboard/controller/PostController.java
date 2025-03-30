package com.domain.openboard.controller;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.*;
import com.domain.openboard.service.PostService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PostResponseDto> addPost(@RequestBody @Valid PostRequestDto dto){
        Post post = postService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PostResponseDto(post));
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

    // 게시글 단건 조회 API
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id){ // URL에서 값을 가져옴
        Post post = postService.findById(id);
        return ResponseEntity.ok().body(new PostResponseDto(post));
    }

    // 게시글 삭제 API
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id, @RequestBody PostPasswordDto dto){
        postService.delete(id,dto.getPassword());
        return ResponseEntity.ok().build();
    }

    // 게시글 수정 API
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePostById(@PathVariable Long id, @RequestBody PostUpdateRequestDto dto){
        Post post = postService.update(id,dto);
        return ResponseEntity.ok().body(new PostResponseDto(post));
    }
}
