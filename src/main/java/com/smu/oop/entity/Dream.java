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

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String interpretation;

    @Enumerated(EnumType.STRING)
    private EmotionCategory emotionCategory;

    @Column(length = 200)
    private String recommendedSongName;

    @Column(length = 100)
    private String recommendedArtist;

    @Column(length = 50)
    private String youtubeVideoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
