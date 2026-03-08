package com.note.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class TagDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Tag name is required")
        @Size(max = 50)
        private String name;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
    }
}
