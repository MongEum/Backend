package com.smu.oop.dto;

import com.smu.oop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private Long id;

    private String nickname;

    private String email;

    private LocalDateTime createdAt;

    private List<DreamResponse> dreams;

    public static UserDetailResponse from(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .dreams(user.getDreams().stream()
                        .map(DreamResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
