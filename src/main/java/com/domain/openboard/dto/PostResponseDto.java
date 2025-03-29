package com.domain.openboard.dto;

import com.domain.openboard.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private final String title;
    private final String content;
    private final String name;
    private final int like_count;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.name = post.getName();
        this.like_count = post.getLikeCount();
        this.created_at = post.getCreate_at();
        this.updated_at = post.getUpdate_at();
    }
}
