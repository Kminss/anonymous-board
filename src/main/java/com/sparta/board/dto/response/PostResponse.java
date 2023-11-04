package com.sparta.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.board.entity.Post;

import java.time.LocalDateTime;
public record PostResponse(
        String name,
        String title,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdDateTime
) {
    public static PostResponse from(Post entity) {
        return new PostResponse(
                entity.getName(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedDateTime()
        );
    }
}
