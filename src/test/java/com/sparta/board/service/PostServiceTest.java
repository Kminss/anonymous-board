package com.sparta.board.service;

import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.dto.response.PostResponse;
import com.sparta.board.entity.Post;
import com.sparta.board.exception.InvalidPasswordException;
import com.sparta.board.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    @DisplayName("게시글 ID로 조회하면 해당하는 게시글 반환")
    @Test
    void givenPostId_whenGetPost_thenReturnPost() {
        // Given
        Long postId = 1L;
        Post post = createPost(postId);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // When
        PostResponse actual = sut.getPost(postId);

        // Then
        assertThat(actual).isEqualTo(PostResponse.from(post));
    }

    @DisplayName("없는 게시글 ID로 조회하면 예외 발생")
    @Test
    void givenNothingAtPostId_whenGetPost_thenThrowException() {
        // Given
        // When & Then
        Assertions.assertThatThrownBy(() -> sut.getPost(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("게시글 수정정보를 입력하면, 게시글을 수정하고 수정된 게시글을 반환한다.")
    @Test
    void givenModifiedPostInfo_whenUpdatingPost_thenUpdatedPost() {
        // Given
        Long postId = 1L;
        Post post = createPost(postId);
        PostRequest request = PostRequest.of("updateName", "testPassword", "updateTitle", "testContent");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        //When
        PostResponse actual = sut.updatePost(postId, request);

        //Then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", request.name())
                .hasFieldOrPropertyWithValue("title", request.title());
    }

    @DisplayName("수정할 게시글의 비밀번호를 다르게 입력하면, 예외를 발생한다.")
    @Test
    void givenInvalidPassword_whenUpdatingPost_thenThrowException() {
        // Given
        Long postId = 1L;
        Post post = createPost(postId);
        ReflectionTestUtils.setField(post, "id", postId);
        PostRequest request = PostRequest.of("updateName", "invalidPassword", "updateTitle", "testContent");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        //When & Then
        Assertions.assertThatThrownBy(() -> sut.updatePost(postId, request))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @DisplayName("없는 게시글의 ID를 입력하면, 예외를 발생한다.")
    @Test
    void givenInvalidPostId_whenUpdatingPost_thenThrowException() {
        // Given
        PostRequest request = PostRequest.of("updateName", "invalidPassword", "updateTitle", "testContent");

        //When & Then
        Assertions.assertThatThrownBy(() -> sut.updatePost(2L, request))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Post createPost(Long postId) {
        Post post = Post.of(
                "testName",
                passwordEncoder.encode("testPassword"),
                "testTitle",
                "testContent"
        );
        ReflectionTestUtils.setField(post, "id", postId);
        return post;
    }
}