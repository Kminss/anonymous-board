package com.sparta.board.service;

import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.entity.Post;
import com.sparta.board.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("게시글 API 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService sut;
    @Mock
    private PostRepository postRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다.")
    @Test
    void givenPostInfo_whenSavingPost_thenReturnSavedPost() {
        // Given
        PostRequest request = PostRequest.of("createName", "testPassword", "createTitle", "crateContent");
        Post post = request.toEntity(passwordEncoder);
        given(postRepository.save(any(Post.class))).willReturn(post);

        // When
        sut.createPost(request);

        // Then
        then(postRepository).should().save(any(Post.class));
    }
}