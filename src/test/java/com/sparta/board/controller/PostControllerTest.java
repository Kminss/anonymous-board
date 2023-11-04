package com.sparta.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.config.AppConfig;
import com.sparta.board.dto.request.PostRequest;
import com.sparta.board.dto.response.PostResponse;
import com.sparta.board.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@Import(AppConfig.class)
@WebMvcTest(PostController.class)
@DisplayName("게시글 API 컨트롤러 테스트")
class PostControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @MockBean
    private PostService postService;

    PostControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper objectMapper,
            @Autowired PasswordEncoder passwordEncoder) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @DisplayName("[Controller][POST] 게시글 생성 필드 유효성 검증 실패")
    void givenPostRequest_whenRequesting_thenFailedValid() throws Exception {
        //given
        PostRequest request = PostRequest.of(
                "",
                "",
                "",
                ""
        );

        //when
        ResultActions actions = mvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        request
                                )

                        )
                        .accept(MediaType.APPLICATION_JSON)
        );

        actions
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.name").value("이름은 최소 2자리 이상이어야합니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 최소 4자리 이상이어야합니다."))
                .andExpect(jsonPath("$.title").value("제목을 입력해주세요."));

    }

    @Test
    @DisplayName("[Controller][POST] 게시글 생성 성공")
    void givenPostRequest_whenRequesting_thenSuccess() throws Exception {
        //given
        PostRequest request = PostRequest.of(
                "testName",
                "testPassword",
                "test Title",
                "test Content"
        );
        given(postService.createPost(request)).willReturn(PostResponse.from(request.toEntity(passwordEncoder)));
        //when
        ResultActions actions = mvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        request
                                )

                        )
                        .accept(MediaType.APPLICATION_JSON)
        );


        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.title").value(request.title()))
                .andExpect(jsonPath("$.content").value(request.content()));

        // auditing 사용으로 실제 db 까지 안가서 날짜는 구분못하는데 괜찮은지?
    }

    @Test
    @DisplayName("[Controller][POST] 게시글 목록 조회 성공")
    void givenNothing_whenRequesting_thenSuccess() throws Exception {
        //given
        ArrayList<PostResponse> response = new ArrayList<>();
        response.add(new PostResponse("testName1", "testTitle1", "testContent1", LocalDateTime.now()));
        response.add(new PostResponse("testName2", "testTitle2", "testContent2", LocalDateTime.now()));
        response.add(new PostResponse("testName3", "testTitle3", "testContent3", LocalDateTime.now()));

        when(postService.getPosts()).thenReturn(response);
        //when
        ResultActions actions = mvc.perform(
                get("/api/posts")
        );

        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testName1")))
                .andExpect(content().string(containsString("testName2")))
                .andExpect(content().string(containsString("testName3")));
    }

    @Test
    @DisplayName("[Controller][POST] 게시글 목록 없는 경우 조회")
    void givenNothing_whenRequesting_thenNoContentSuccess() throws Exception {
        //given
        when(postService.getPosts()).thenReturn(List.of());
        //when
        ResultActions actions = mvc.perform(
                get("/api/posts")
        );

        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
}