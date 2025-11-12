package com.smu.oop.controller;

import com.smu.oop.dto.ApiResponse;
import com.smu.oop.dto.UserDetailResponse;
import com.smu.oop.service.DreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final DreamService dreamService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetail(
            @PathVariable Long userId) {
        UserDetailResponse response = dreamService.getUserWithDreams(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
