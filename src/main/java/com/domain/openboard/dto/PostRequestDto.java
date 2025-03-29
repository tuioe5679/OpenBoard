package com.domain.openboard.dto;

import com.domain.openboard.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자
@AllArgsConstructor// 모든 필드 값을 파파미터로 받는 생성자
@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private String name;
    private String password;

    public Post toEntity(String hashedPassword) { // 생성자를 통해 객체 생성
        return Post.builder()
                .title(title)
                .content(content)
                .name(name)
                .password(hashedPassword)
                .build();
    }
}
