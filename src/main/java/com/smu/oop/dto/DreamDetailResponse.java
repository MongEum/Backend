package com.smu.oop.dto;

import com.smu.oop.entity.Dream;
import com.smu.oop.entity.EmotionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DreamDetailResponse {

    private Long id;

    private String title;

    private String content;

    private String interpretation;

    private EmotionCategory emotionCategory;

    private String emotionalAnalysis;

    private String recommendedSongName;

    private String recommendedArtist;

    private String recommendedSongUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserResponse user;

    public static DreamDetailResponse from(Dream dream) {
        return DreamDetailResponse.builder()
                .id(dream.getId())
                .title(dream.getTitle())
                .content(dream.getContent())
                .interpretation(dream.getInterpretation())
                .emotionCategory(dream.getEmotionCategory())
                .emotionalAnalysis(dream.getEmotionalAnalysis())
                .recommendedSongName(dream.getRecommendedSongName())
                .recommendedArtist(dream.getRecommendedArtist())
                .recommendedSongUrl(dream.getRecommendedSongUrl())
                .createdAt(dream.getCreatedAt())
                .updatedAt(dream.getUpdatedAt())
                .user(UserResponse.from(dream.getUser()))
                .build();
    }
}
