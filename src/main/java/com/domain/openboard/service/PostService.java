package com.domain.openboard.service;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.PostRequestDto;
import com.domain.openboard.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // final이 붙거나 @NoNull이 붙은 필드의 생성자 추가
@Transactional // 클래스 전체 트랜잭션 적용
public class PostService {

    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    // 게시글 작성
    public Post save(PostRequestDto dto){
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        return postRepository.save(dto.toEntity(hashedPassword));
    }

    // 게시글 전체 조회
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    // 게시글 단건 조회
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found" + id)); // 엔티티를 조회하고 없으면 예외 발생
    }
}
