package com.domain.openboard.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity // 엔티티로 지정
@Getter
@Table(name = "Post")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 (protected 제한하여 무분별한 객체 생성을 방지)
@EntityListeners(AuditingEntityListener.class)// 엔티티 리스너를 적용 (Auditing 기능을 포함시킨다) @CreateDate 기능 사용시 필요
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name = "post_id", updatable = false)
    private Long post_id;

    @Column(name = "title", nullable = false,length = 200)
    private String title;

    @Lob // Large Object 긴 문자열에 사용
    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "name" ,nullable = false)
    private String name;

    @Column(name = "like_count",nullable = false)
    private int likeCount;

    @Column(name = "password",nullable = false)
    private String password;

    @CreatedDate // 엔티티가 처음 저장될 때의 시간 자동 기록
    @Column(name = "create_at",updatable = false)
    private LocalDateTime create_at;

    @LastModifiedDate // 엔티티가 수정될 때의 시간 자동 기록
    @Column(name = "updated_at")
    private LocalDateTime update_at;

    // 빌더 패턴 적용
    @Builder
    public Post(String title, String content, String name,String password) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.likeCount = 0; // 기본 좋아요 개수는 0개
        this.password = password;
    }
}
