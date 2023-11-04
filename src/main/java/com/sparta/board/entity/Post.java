package com.sparta.board.entity;

import com.sparta.board.dto.request.PostRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post")
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String title;
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    public Post(String name, String password, String title, String content) {
        this.name = name;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public static Post of(String name, String password, String title, String content) {
        return new Post(name, password, title, content);
    }

    public void updatePost(PostRequest request) {
        this.name = request.name();
        this.title = request.title();
        this.content = request.content();
    }
}
