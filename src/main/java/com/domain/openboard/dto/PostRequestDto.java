package com.domain.openboard.dto;

import com.domain.openboard.domain.Post;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자
@AllArgsConstructor// 모든 필드 값을 파파미터로 받는 생성자
@Getter
public class PostRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200,message = "제목은 200자 이하로 작성해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotBlank(message = "작성자는 필수입니다.")
    @Size(max = 20,message = "작성자는 20자 이하로 작성해주세요.")
    private String name;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6,max = 20,message = "비밀번호는 6자 이상 20자 이하로 작성해주세요.")
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