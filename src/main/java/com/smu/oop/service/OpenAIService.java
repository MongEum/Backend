package com.smu.oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.oop.entity.EmotionCategory;
import com.smu.oop.exception.BusinessException;
import com.smu.oop.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> interpretDream(String dreamContent) {
        String prompt = createDreamInterpretationPrompt(dreamContent);
        String response = callOpenAI(prompt);
        return parseDreamInterpretation(response);
    }

    public Map<String, String> recommendMusic(String dreamContent) {
        String prompt = createMusicRecommendationPrompt(dreamContent);
        String response = callOpenAI(prompt);
        return parseMusicRecommendation(response);
    }

    private String createDreamInterpretationPrompt(String dreamContent) {
        return """
            당신은 꿈 해몽 전문가입니다. 사용자가 꾼 꿈을 분석하여 통합적인 해석을 제공해 주세요.

            사용자의 꿈 내용: "%s"

            다음 세 가지 관점을 모두 포함하여 하나의 통합된 해석을 작성해 주세요:
            1. 동양적 관점: 한국, 중국, 일본의 전통적인 꿈 해석 방식 (길몽/흉몽, 상징적 의미)
            2. 서양적 관점: 프로이트, 융의 정신분석학적 관점 (무의식의 욕구, 상징의 의미)
            3. 심리적 관점: 현재 심리 상태 분석 (스트레스 요인, 내면의 갈등, 조언)

            또한 꿈의 전반적인 감정을 다음 7가지 카테고리 중 하나로 분류해 주세요:
            - HAPPY: 행복하고 즐거운 꿈
            - SAD: 슬프고 우울한 꿈
            - ANXIOUS: 불안하고 걱정스러운 꿈
            - PEACEFUL: 평화롭고 안정적인 꿈
            - EXCITED: 흥분되고 자극적인 꿈
            - NOSTALGIC: 그리움과 향수를 느끼는 꿈
            - FEARFUL: 두렵고 무서운 꿈

            다음 형식으로 정확히 응답해 주세요 (JSON 형식):
            {
                "interpretation": "동양적, 서양적, 심리적 관점을 모두 포함한 통합 해석. 각 관점을 구분하여 3-4문단으로 상세히 작성해 주세요. 예: '동양적 관점에서 보면...', '서양 정신분석학의 관점에서는...', '현재 심리 상태를 분석하면...' 형식으로 작성",
                "emotionCategory": "HAPPY, SAD, ANXIOUS, PEACEFUL, EXCITED, NOSTALGIC, FEARFUL 중 정확히 하나만 선택"
            }

            반드시 JSON 형식으로만 응답하고, emotionCategory는 위 7가지 중 정확히 하나만 사용하세요.
            """.formatted(dreamContent);
    }

    private String createMusicRecommendationPrompt(String dreamContent) {
        return """
            당신은 음악 추천 전문가입니다. 사용자가 꾼 꿈의 분위기와 감정을 분석하여 어울리는 음악을 추천해 주세요.

            사용자의 꿈 내용: "%s"

            꿈의 분위기, 감정, 주제를 고려하여 가장 잘 어울리는 음악 1곡을 추천해 주세요.
            유명하고 YouTube에서 쉽게 찾을 수 있는 곡으로 추천해 주세요.

            다음 형식으로 정확히 응답해 주세요 (JSON 형식):
            {
                "title": "노래 제목",
                "artist": "아티스트명",
                "reason": "이 곡을 추천하는 이유 (꿈의 분위기와 어떻게 연결되는지 1-2문장으로 설명)"
            }

            반드시 JSON 형식으로만 응답하고, 다른 텍스트는 포함하지 마세요.
            """.formatted(dreamContent);
    }

    private String callOpenAI(String prompt) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(apiUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .build();

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(message));
            requestBody.put("temperature", 0.7);

            String response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            log.error("OpenAI API call failed", e);
            throw new BusinessException(ErrorCode.OPENAI_SERVICE_ERROR, e.getMessage());
        }
    }

    private Map<String, String> parseDreamInterpretation(String response) {
        Map<String, String> result = new HashMap<>();
        try {
            String cleanedResponse = cleanJsonResponse(response);
            JsonNode jsonNode = objectMapper.readTree(cleanedResponse);

            result.put("interpretation", jsonNode.get("interpretation").asText());

            // EmotionCategory 검증
            String emotionCategory = jsonNode.get("emotionCategory").asText().toUpperCase();
            try {
                EmotionCategory.valueOf(emotionCategory);
                result.put("emotionCategory", emotionCategory);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid emotion category from AI: {}. Defaulting to PEACEFUL", emotionCategory);
                result.put("emotionCategory", "PEACEFUL");
            }
        } catch (Exception e) {
            log.error("Failed to parse dream interpretation", e);
            throw new BusinessException(ErrorCode.OPENAI_SERVICE_ERROR, "Failed to parse AI response: " + e.getMessage());
        }
        return result;
    }

    private Map<String, String> parseMusicRecommendation(String response) {
        Map<String, String> result = new HashMap<>();
        try {
            String cleanedResponse = cleanJsonResponse(response);
            JsonNode jsonNode = objectMapper.readTree(cleanedResponse);

            result.put("title", jsonNode.get("title").asText());
            result.put("artist", jsonNode.get("artist").asText());
            result.put("reason", jsonNode.has("reason") ? jsonNode.get("reason").asText() : "");
        } catch (Exception e) {
            log.error("Failed to parse music recommendation", e);
            throw new BusinessException(ErrorCode.OPENAI_SERVICE_ERROR, "Failed to parse music recommendation: " + e.getMessage());
        }
        return result;
    }

    private String cleanJsonResponse(String response) {
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        }
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }
}
