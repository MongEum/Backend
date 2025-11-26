package com.smu.oop.service;

import com.smu.oop.dto.DreamDetailResponse;
import com.smu.oop.dto.DreamRequest;
import com.smu.oop.dto.DreamResponse;
import com.smu.oop.dto.UserDetailResponse;
import com.smu.oop.entity.Dream;
import com.smu.oop.entity.EmotionCategory;
import com.smu.oop.entity.User;
import com.smu.oop.exception.BusinessException;
import com.smu.oop.exception.ErrorCode;
import com.smu.oop.repository.DreamRepository;
import com.smu.oop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DreamService {

    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;
    private final YouTubeService youTubeService;

    @Transactional
    public DreamResponse createDream(Long userId, DreamRequest request) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        log.info("Creating dream for user: {}, title: {}", userId, request.getTitle());

        // Call OpenAI for dream interpretation and emotion category
        Map<String, String> interpretationResult = openAIService.interpretDream(request.getContent());
        String interpretation = interpretationResult.get("interpretation");
        EmotionCategory emotionCategory = EmotionCategory.valueOf(interpretationResult.get("emotionCategory"));

        log.info("AI interpretation completed. Emotion: {}", emotionCategory);

        // Call OpenAI for music recommendation
        Map<String, String> musicResult = openAIService.recommendMusic(request.getContent());
        String musicTitle = musicResult.get("title");
        String musicArtist = musicResult.get("artist");
        String musicReason = musicResult.get("reason");

        log.info("Music recommendation: {} - {}", musicArtist, musicTitle);

        // Search YouTube for the recommended song
        String youtubeVideoId = youTubeService.searchYouTubeVideoId(musicTitle, musicArtist);

        log.info("YouTube video found: {}", youtubeVideoId);

        // Create dream with AI-generated data
        Dream dream = Dream.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .interpretation(interpretation)
                .emotionCategory(emotionCategory)
                .recommendedSongName(musicTitle)
                .recommendedArtist(musicArtist)
                .youtubeVideoId(youtubeVideoId)
                .user(user)
                .build();

        Dream savedDream = dreamRepository.save(dream);
        log.info("Dream created successfully with ID: {}", savedDream.getId());

        return DreamResponse.from(savedDream);
    }

    @Transactional(readOnly = true)
    public List<DreamResponse> getMyDreams(Long userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "ID: " + userId);
        }

        List<Dream> dreams = dreamRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return dreams.stream()
                .map(DreamResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DreamDetailResponse getDreamDetail(Long dreamId, Long currentUserId) {
        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DREAM_NOT_FOUND, "ID: " + dreamId));

        // Check access permission
        if (!dream.getUser().getId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.DREAM_ACCESS_DENIED, "Dream ID: " + dreamId);
        }

        return DreamDetailResponse.from(dream);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserWithDreams(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        return UserDetailResponse.from(user);
    }

    @Transactional
    public void deleteDream(Long dreamId, Long currentUserId) {
        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DREAM_NOT_FOUND, "ID: " + dreamId));

        // Check access permission
        if (!dream.getUser().getId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.DREAM_ACCESS_DENIED, "Dream ID: " + dreamId);
        }

        dreamRepository.delete(dream);
    }
}
