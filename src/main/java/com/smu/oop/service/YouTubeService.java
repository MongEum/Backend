package com.smu.oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.oop.exception.BusinessException;
import com.smu.oop.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * YouTube에서 음악 비디오를 검색하여 videoId를 반환합니다.
     * @param title 노래 제목
     * @param artist 아티스트명
     * @return YouTube videoId, 실패 시 null
     */
    public String searchYouTubeVideoId(String title, String artist) {
        try {
            String searchQuery = URLEncoder.encode(artist + " " + title + " official", StandardCharsets.UTF_8);

            WebClient webClient = WebClient.builder()
                    .baseUrl("https://www.googleapis.com")
                    .build();

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/youtube/v3/search")
                            .queryParam("part", "snippet")
                            .queryParam("q", searchQuery)
                            .queryParam("type", "video")
                            .queryParam("maxResults", 1)
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode items = jsonNode.get("items");

            if (items != null && items.size() > 0) {
                String videoId = items.get(0).get("id").get("videoId").asText();
                log.info("Found YouTube video: {} - {}, videoId: {}", artist, title, videoId);
                return videoId;
            }

            log.warn("No YouTube video found for: {} - {}", artist, title);
            return null;
        } catch (Exception e) {
            log.error("Failed to search YouTube video for: {} - {}", artist, title, e);
            throw new BusinessException(ErrorCode.YOUTUBE_SERVICE_ERROR, e.getMessage());
        }
    }
}
