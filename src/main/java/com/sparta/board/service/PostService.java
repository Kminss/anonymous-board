package com.sparta.board.service;

import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.dto.response.PostResponse;
import com.sparta.board.entity.Post;
import com.sparta.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return null;
    }

    public PostResponse getPost(Long id) {
       return null;
    }

    public PostResponse updatePost(Long postId, PostRequest request) {
        return null;
    }

    public void deletePost(Long id, String password) {
    }
}