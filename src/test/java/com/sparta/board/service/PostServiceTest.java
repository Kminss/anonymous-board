package com.sparta.board.service;

import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.dto.response.PostResponse;
import com.sparta.board.entity.Post;
import com.sparta.board.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
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

    @DisplayName("게시글이 0개일 때, 목록 조회")
    @Test
    void givenNothing_whenGetPosts_thenReturnEmptyList() {
        // Given

        // When
        List<PostResponse> response = sut.getPosts();

        // Then
        assertThat(response).isEmpty();
    }

    @DisplayName("게시글이 3개일 때, 목록 조회 시 게시글 반환")
    @Test
    void givenNothing_whenGetPosts_thenReturnPosts() {
        // Given
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post("testName1", "","testTitle1", "testContent1"));
        posts.add(new Post("testName2", "","testTitle2", "testContent2"));
        posts.add(new Post("testName3", "","testTitle3", "testContent3"));

        given(postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDateTime"))).willReturn(posts);
        // When
        List<PostResponse> result = sut.getPosts();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting("name","title","content")
                .containsExactly(
                        tuple("testName1","testTitle1","testContent1"),
                        tuple("testName2","testTitle2","testContent2"),
                        tuple("testName3","testTitle3","testContent3")
                );
    }
}