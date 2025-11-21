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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DreamService {

    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;

    @Transactional
    public DreamResponse createDream(Long userId, DreamRequest request) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        // Generate temporary AI responses (AI integration placeholders)
        String interpretation = generateTemporaryInterpretation(request.getContent());
        EmotionCategory emotionCategory = generateTemporaryEmotionCategory();
        String emotionalAnalysis = generateTemporaryEmotionalAnalysis(emotionCategory);

        // Create dream
        Dream dream = Dream.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .interpretation(interpretation)
                .emotionCategory(emotionCategory)
                .emotionalAnalysis(emotionalAnalysis)
                .recommendedSongName("Moonlight Sonata")
                .recommendedArtist("Ludwig van Beethoven")
                .recommendedSongUrl("https://www.youtube.com/watch?v=4Tr0otuiQuU")
                .user(user)
                .build();

        Dream savedDream = dreamRepository.save(dream);

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

    /**
     * Temporary interpretation generator (placeholder for AI integration)
     */
    private String generateTemporaryInterpretation(String dreamContent) {
        return "해몽 결과: 이 꿈은 AI 해몽 서버와 연동되면 자동으로 분석됩니다. " +
                "현재는 임시 해몽 메시지가 표시됩니다. " +
                "꿈의 내용을 바탕으로 AI가 상징적 의미와 심리적 해석을 제공할 예정입니다.";
    }

    /**
     * Temporary emotion category generator (placeholder for AI integration)
     */
    private EmotionCategory generateTemporaryEmotionCategory() {
        return EmotionCategory.PEACEFUL;
    }

    /**
     * Temporary emotional analysis generator (placeholder for AI integration)
     */
    private String generateTemporaryEmotionalAnalysis(EmotionCategory category) {
        return String.format("감정 분석 결과: 이 꿈은 '%s'한 감정을 나타냅니다. " +
                "AI 서버와 연동되면 더 상세한 감정 분석이 제공됩니다. " +
                "꿈의 내용과 맥락을 바탕으로 감정의 원인과 의미를 분석할 예정입니다.",
                category.getKoreanName());
    }
}
