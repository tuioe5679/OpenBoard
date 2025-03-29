package com.domain.openboard.dto;

import lombok.Getter;

@Getter
public class PostListResponseDto {

    private final String title;
    private final String content;
    private final String name;

    public PostListResponseDto(String title, String content, String name) {
        this.title = title;
        this.content = content;
        this.name = name;
    }
}
