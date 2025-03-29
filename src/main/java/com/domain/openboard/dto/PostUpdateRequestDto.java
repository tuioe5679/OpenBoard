package com.domain.openboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String password;
}
