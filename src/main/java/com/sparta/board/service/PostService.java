package com.sparta.board.service;

import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.dto.response.PostResponse;
import com.sparta.board.entity.Post;
import com.sparta.board.exception.InvalidPasswordException;
import com.sparta.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public PostResponse createPost(PostRequest request) {
        Post post = postRepository.save(request.toEntity(passwordEncoder));
        return PostResponse.from(post);
    }

    public List<PostResponse> getPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDateTime")).stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPost(Long id) {
        return PostResponse.from(
                postRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("조회할 게시글이 없습니다."))
        );
    }

    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("수정할 게시글이 없습니다."));

        if (!passwordEncoder.matches(request.password(), post.getPassword())) {
            throw new InvalidPasswordException();
        }

        post.updatePost(request);

        return PostResponse.from(post);
    }

    public void deletePost(Long id, String password) {
    }
}
