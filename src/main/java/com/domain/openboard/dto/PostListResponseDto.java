package com.domain.openboard.dto;

import com.domain.openboard.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PostListResponseDto {

    @Schema(description = "게시글 제목",example = "제목")
    private final String title;

    @Schema(description = "게시글 내용",example = "내용")
    private final String content;

    @Schema(description = "게시글 작성자",example = "이름")
    private final String name;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.name = post.getName();
    }
}
