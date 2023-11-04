package com.sparta.board.dto.request;

import com.sparta.board.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

public record PostRequest(
        @Size(min = 2, message = "이름은 최소 2자리 이상이어야합니다.")
        String name,
        @Size(min = 4, message = "비밀번호는 최소 4자리 이상이어야합니다.")
        String password,
        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        String content
) {

    public static PostRequest of(String name, String password, String title, String content) {
        return new PostRequest(name, password, title, content);
    }

    public Post toEntity(PasswordEncoder passwordEncoder) {
        return Post.of(name, passwordEncoder.encode(password), title, content);
    }
}
