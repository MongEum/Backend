package com.smu.oop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmotionCategory {

    HAPPY("행복"),
    SAD("슬픔"),
    ANXIOUS("불안"),
    PEACEFUL("평온"),
    EXCITED("흥분"),
    NOSTALGIC("그리움"),
    FEARFUL("공포");

    private final String koreanName;
}
