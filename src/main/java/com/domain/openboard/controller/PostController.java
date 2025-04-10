package com.domain.openboard.controller;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.*;
import com.domain.openboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Post API",description = "작성글 관련 기능 API")
@RequiredArgsConstructor
@RequestMapping("/api") // 전체 기본 경로
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 변환
public class PostController {

    private final PostService postService;

    // 게시글 작성 API
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다")
    @ApiResponse(responseCode = "201",description = "등록 성공")
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> addPost(@RequestBody @Valid PostRequestDto dto){
        Post post = postService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PostResponseDto(post));
    }

    // 게시글 목록 조회 API
    @Operation(summary = "게시글 목록 조회",description = "게시글 전체 목록을 조회합니다")
    @ApiResponse(responseCode = "200",description = "조회 성공")
    @GetMapping("/posts")
    public ResponseEntity<List<PostListResponseDto>> findAllPosts() {
        List<PostListResponseDto> postListDto = postService.findAll()
                .stream()                               // 컬렉션(List)를 Stream 객체로 변환
                .map(PostListResponseDto::new)          // 각 Post 객체를 PostListResponseDto로 변환
                .toList();                              // 변환된 요소들을 새로운 리스트(List<PostListResponseDto>)로 수집
                                                        // List<Post> -> Stream -> PostListResponseDto -> List<PostListResponseDto>
        return ResponseEntity.ok().body(postListDto);
    }

    // 게시글 단건 조회 API
    @Operation(summary = "게시글 조회",description = "게시글 id로 단건 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "조회 성공"),
            @ApiResponse(responseCode = "404",description = "해당 게시글을 찾을 수 없음")
    })
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> findPost(@Parameter(description = "조회할 게시글 ID",example = "1",required = true) @PathVariable Long id){ // URL에서 값을 가져옴
        Post post = postService.findById(id);
        return ResponseEntity.ok().body(new PostResponseDto(post));
    }

    // 게시글 삭제 API
    @Operation(summary = "게시글 삭제",description = "게시글을 삭제합니다 작성한 게시글의 비밀번호,글 번호를 받습니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "삭제 성공"),
            @ApiResponse(responseCode = "404",description = "해당 게시글을 찾을 수 없음")
    })
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@Parameter(description = "삭제할 게시글 ID",example = "1",required = true)@PathVariable Long id, @RequestBody PostPasswordDto dto){
        postService.delete(id,dto.getPassword());
        return ResponseEntity.ok().build();
    }

    // 게시글 수정 API
    @Operation(summary = "게시글 수정",description = "게시글을 수정합니다 작성한 게시글의 비밀번호,글 번호를 받습니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "수정 성공"),
            @ApiResponse(responseCode = "404",description = "해당 게시글을 찾을 수 없음")
    })
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@Parameter(description = "수정할 게시글 ID",example = "1",required = true)@PathVariable Long id, @RequestBody PostUpdateRequestDto dto){
        Post post = postService.update(id,dto);
        return ResponseEntity.ok().body(new PostResponseDto(post));
    }
}
