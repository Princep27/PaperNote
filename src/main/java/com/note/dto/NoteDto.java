package com.note.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

public class NoteDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        private String title;

        private String content;
    }

    @Data
    public static class UpdateRequest {
        @Size(max = 255)
        private String title;

        private String content;
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private boolean pinned;
        private boolean favorite;
        private boolean deleted;
        private LocalDateTime deletedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Set<TagDto.Response> tags;
        private String sharePublicId;
    }
}
