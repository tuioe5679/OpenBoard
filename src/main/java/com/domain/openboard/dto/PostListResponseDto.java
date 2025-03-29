package com.domain.openboard.dto;

import com.domain.openboard.domain.Post;
import lombok.Getter;

@Getter
public class PostListResponseDto {

    private final String title;
    private final String content;
    private final String name;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.name = post.getName();
    }
}
