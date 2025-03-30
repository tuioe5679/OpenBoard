package com.domain.openboard.service;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.PostRequestDto;
import com.domain.openboard.dto.PostUpdateRequestDto;
import com.domain.openboard.error.exception.PostNotFoundException;
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
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    // 게시글 삭제
    public void delete(Long id,String inputPassword){
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        // 입력 받은 password와 게시글에 저장된 password를 비교
        if(!passwordEncoder.matches(inputPassword,post.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        postRepository.delete(post);
    }

    // 게시글 수정
    public Post update(Long id, PostUpdateRequestDto dto){
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        if(!passwordEncoder.matches(dto.getPassword(),post.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        post.update(dto.getTitle(),dto.getContent());
        return post;
    }
}
