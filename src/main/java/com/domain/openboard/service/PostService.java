package com.domain.openboard.service;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.PostRequestDto;
import com.domain.openboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final이 붙거나 @NoNull이 붙은 필드의 생성자 추가
public class PostService {

    private final PostRepository postRepository;

    public Post save(PostRequestDto dto){
        return postRepository.save(dto.toEntity());
    }
}
