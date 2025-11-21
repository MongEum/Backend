package com.smu.oop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DreamRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 32, message = "Title must not exceed 32 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;
}
