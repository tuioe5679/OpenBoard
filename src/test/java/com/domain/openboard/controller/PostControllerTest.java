package com.domain.openboard.controller;

import com.domain.openboard.domain.Post;
import com.domain.openboard.dto.PostPasswordDto;
import com.domain.openboard.dto.PostRequestDto;
import com.domain.openboard.dto.PostUpdateRequestDto;
import com.domain.openboard.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    //  테스트용 HTTP 요청을 수행할 MockMvc 객체 (Spring MVC 환경을 흉내냄)
    @Autowired
    protected MockMvc mockMvc;

    // 객체 → JSON, JSON → 객체 변환을 위한 Jackson Mapper
    @Autowired
    protected ObjectMapper objectMapper;

    // Spring 컨텍스트(WebApplicationContext) 주입 → MockMvc 설정에 필요
    @Autowired
    private WebApplicationContext webApplicationContext;

    // 실제 DB 접근을 위한 Repository (테스트 중 직접 조회/삭제 등에 사용)
    @Autowired
    PostRepository postRepository;

    // 비밀번호 암호화를 위한 PasswordEncoder
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void mockMvcSetup() {
        // 테스트 시작 전마다 MockMvc를 WebApplicationContext 기반으로 다시 세팅
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        // 테스트 격리를 위해 모든 게시글 데이터를 초기화 (DB clean up)
        postRepository.deleteAll();
    }

    @DisplayName("addPost: 게시글 작성에 성공한다")
    @Test
    public void addPost() throws Exception {

        // Given (테스트 준비)

        // 게시글 추가에 필요한 요청 객체를 만들기
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";
        final String name = "name";
        final String password = "password";
        final PostRequestDto dto = new PostRequestDto(title, content, name, password);

        // 객체 JSON으로 직렬화 (DTO를 JSON 문자열로 변환)
        final String requestBody = objectMapper.writeValueAsString(dto);

        // when (테스트 진행)

        // JSON 형태로 POST 요청 전송
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // Content-Type : application_json_value
                .content(requestBody));                         // 요청 본문

        // then (테스트의 결과를 검증)

        // HTTP 응답 코드가 201 Created인지 검증
        resultActions.andExpect(status().isCreated());

        // 실제 DB에 저장된 데이터
        List<Post> posts = postRepository.findAll();

        // 게시글이 하나 저장되어 있는지 검증
        assertThat(posts.size()).isEqualTo(1);

        // 저장된 게시글의 필드가 입력값과 같은지 검증
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
        assertThat(posts.get(0).getName()).isEqualTo(name);

        // 저장된 비밀번호는 암호화 되어 있어 matches() 메서드로 입력 비밀번호와 같은지 검증
        assertThat(passwordEncoder.matches(password, posts.get(0).getPassword())).isTrue();
    }

    @DisplayName("findAllPosts: 게시글 목록 조회에 성공한다")
    @Test
    public void findAllPosts() throws Exception {

        // Given (테스트 준비)
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";
        final String name = "name";
        final String password = "password";

        // 게시글을 DB에 직접 저장 (조회 테스트용)
        postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .name(name)
                .password(password)  // 이 테스트에선 암호화 X
                .build());

        // when (테스트 진행)

        // 게시글 목록 조회 GET 요청 (Accept: application/json)
        final ResultActions resultActions = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON_VALUE));

        // then (테스트의 결과를 검증)
        resultActions
                // 1. 응답 코드가 200 OK인지 확인
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @DisplayName("findPost: 게시글 단건 조회을 성공한다")
    @Test
    public void findPost() throws Exception {

        // Given (테스트 준비)
        final String url = "/api/posts/{id}";
        final String title = "title";
        final String content = "content";
        final String name = "name";
        final String password = "password";

        // 게시글을 DB에 직접 저장 (조회 테스트용)
        Post post = postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .name(name)
                .password(password) // 이 테스트에선 암호화 X
                .build());

        // when (테스트 진행)

        // 게시글 목록 조회 GET 요청
        final ResultActions resultActions = mockMvc.perform(get(url,post.getPost_id()));

        // then (테스트의 결과를 검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.name").value(name));

    }

    @DisplayName("deletePost: 게시글 삭제에 성공한다")
    @Test
    public void deletePost() throws Exception {

        // Given (테스트 준비)
        final String url = "/api/posts/{id}";
        final String title = "title";
        final String content = "content";
        final String name = "name";
        final String password = "password";

        // 게시글을 DB에 직접 저장 (조회 테스트용)
        Post savePost = postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .name(name)
                .password(passwordEncoder.encode(password)) // 서비스 계층을 거치지 않아서 암호화된 패스워드 저장
                .build());

        // 비밀번호를 포함한 요청 DTO 생성
        final PostPasswordDto postPasswordDto = new PostPasswordDto(password);
        String requestBody = objectMapper.writeValueAsString(postPasswordDto);

        // when (테스트 진행)
        // Delete 메서드로 요청, contentType = JSON
        mockMvc.perform(delete(url,savePost.getPost_id())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk());

        Optional<Post> post = postRepository.findById(savePost.getPost_id());

        // then (테스트의 결과를 검증)

        // 삭제한 글이 존재하는지 검증
        Assertions.assertThat(post).isEmpty();
    }

    @DisplayName("updatePost: 게시글 수정에 성공한다")
    @Test
    public void updateArticle() throws Exception {

        // Given (테스트 준비)
        final String url = "/api/posts/{id}";
        final String title = "title";
        final String content = "content";
        final String name = "name";
        final String password = "password";

        // 게시글을 DB에 직접 저장 (조회 테스트용)
        Post savePost = postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .name(name)
                .password(passwordEncoder.encode(password)) // 서비스 계층을 거치지 않아서 암호화된 패스워드 저장
                .build());

        // 수정한 데이터
        final String updateTitle = "updateTitle";
        final String updateContent = "updateContent";

        PostUpdateRequestDto request = new PostUpdateRequestDto(updateTitle, updateContent,password);
        String requestBody = objectMapper.writeValueAsString(request);

        // when (테스트 진행)
        // PUT 메서드로 요청, contentType = JSON
        ResultActions result = mockMvc.perform(put(url,savePost.getPost_id())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then (테스트의 결과를 검증)
        result.andExpect(status().isOk());

        Post post = postRepository.findById(savePost.getPost_id()).get();

        // 기존값과 수정된 값이 같은지 검증
        Assertions.assertThat(post.getTitle()).isEqualTo(updateTitle);
        Assertions.assertThat(post.getContent()).isEqualTo(updateContent);
    }

    @DisplayName("addPost: 제목이 비어있으면 VALIDATION_ERROR 발생")
    @Test
    public void addPostBlankTitleValidationError() throws Exception {
        // Given (테스트 준비)

        // 게시글 추가에 필요한 요청 객체를 만들기
        final String url = "/api/posts";
        final String title = "";
        final String content = "content";
        final String name = "name";
        final String password = "password";
        final PostRequestDto dto = new PostRequestDto(title,content,name,password);

        // 객체 JSON으로 직렬화 (DTO를 JSON 문자열로 변환)
        final String requestBody = objectMapper.writeValueAsString(dto);

        // when (테스트 진행)

        // JSON 형태로 POST 요청 전송
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // Content-Type : application_json_value
                .content(requestBody));                         // 요청 본문

        // then (테스트의 결과를 검증)
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("제목은 필수입니다."));
    }

    @DisplayName("addPost: 내용이 비어있으면 VALIDATION_ERROR 발생")
    @Test
    public void addPostBlankContentValidationError() throws Exception {
        // Given (테스트 준비)

        // 게시글 추가에 필요한 요청 객체를 만들기
        final String url = "/api/posts";
        final String title = "title";
        final String content = "";
        final String name = "name";
        final String password = "password";
        final PostRequestDto dto = new PostRequestDto(title,content,name,password);

        // 객체 JSON으로 직렬화 (DTO를 JSON 문자열로 변환)
        final String requestBody = objectMapper.writeValueAsString(dto);

        // when (테스트 진행)

        // JSON 형태로 POST 요청 전송
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // Content-Type : application_json_value
                .content(requestBody));                         // 요청 본문

        // then (테스트의 결과를 검증)
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("내용은 필수입니다."));
    }

    @DisplayName("addPost: 작성자가 20자가 넘으면 VALIDATION_ERROR 발생")
    @Test
    public void addPostOverNameValidationError() throws Exception {
        // Given (테스트 준비)

        // 게시글 추가에 필요한 요청 객체를 만들기
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";
        final String name = "a".repeat(21);
        final String password = "password";
        final PostRequestDto dto = new PostRequestDto(title,content,name,password);

        // 객체 JSON으로 직렬화 (DTO를 JSON 문자열로 변환)
        final String requestBody = objectMapper.writeValueAsString(dto);

        // when (테스트 진행)

        // JSON 형태로 POST 요청 전송
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // Content-Type : application_json_value
                .content(requestBody));                         // 요청 본문

        // then (테스트의 결과를 검증)
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("작성자는 20자 이하로 작성해주세요."));
    }
}