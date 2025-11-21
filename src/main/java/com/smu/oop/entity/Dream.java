package com.smu.oop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "dreams")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Dream extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String interpretation;

    @Enumerated(EnumType.STRING)
    private EmotionCategory emotionCategory;

    @Column(columnDefinition = "TEXT")
    private String emotionalAnalysis;

    private String recommendedSongName;

    private String recommendedArtist;

    private String recommendedSongUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
