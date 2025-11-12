package com.smu.oop.controller;

import com.smu.oop.dto.*;
import com.smu.oop.entity.User;
import com.smu.oop.service.DreamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dreams")
@RequiredArgsConstructor
public class DreamController {

    private final DreamService dreamService;

    @PostMapping
    public ResponseEntity<ApiResponse<DreamResponse>> createDream(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DreamRequest request) {
        DreamResponse response = dreamService.createDream(user.getId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dream created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DreamResponse>>> getMyDreams(
            @AuthenticationPrincipal User user) {
        List<DreamResponse> response = dreamService.getMyDreams(user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DreamDetailResponse>> getDreamDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        DreamDetailResponse response = dreamService.getDreamDetail(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDream(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        dreamService.deleteDream(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Dream deleted successfully", null));
    }
}
