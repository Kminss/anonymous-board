package com.sparta.board.controller;

import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.dto.response.PostResponse;
import com.sparta.board.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("/api/posts")
@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody @Valid PostRequest request,
            BindingResult bindingResult
    ) {
        //유효성 검증
        if (bindingResult.hasErrors()) {
            HashMap<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors()
                    .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));

    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable(value = "postId") Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable(value = "postId") Long id,
            @RequestBody PostRequest request
    ) {
        return null;

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(
            @PathVariable(value = "postId") Long id,
            @RequestHeader("password") String password
    ) {
        return null;
    }
}
