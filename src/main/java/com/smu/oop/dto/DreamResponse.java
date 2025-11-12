package com.smu.oop.dto;

import com.smu.oop.entity.Dream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DreamResponse {

    private Long id;

    private String title;

    private String content;

    private String interpretation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static DreamResponse from(Dream dream) {
        return DreamResponse.builder()
                .id(dream.getId())
                .title(dream.getTitle())
                .content(dream.getContent())
                .interpretation(dream.getInterpretation())
                .createdAt(dream.getCreatedAt())
                .updatedAt(dream.getUpdatedAt())
                .build();
    }
}
