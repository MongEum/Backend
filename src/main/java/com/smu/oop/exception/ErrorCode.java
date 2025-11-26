package com.smu.oop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User related errors
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "User already exists with this email"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U003", "Invalid email or password"),

    // Dream related errors
    DREAM_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "Dream not found"),
    DREAM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "D002", "Access denied to this dream"),

    // Authentication errors
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "Authentication required"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "Invalid or expired token"),

    // AI Service errors
    OPENAI_SERVICE_ERROR(HttpStatus.BAD_GATEWAY, "A003", "OpenAI service error"),
    YOUTUBE_SERVICE_ERROR(HttpStatus.BAD_GATEWAY, "A004", "YouTube service error"),

    // Validation errors
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "V001", "Invalid input"),

    // Server errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "Internal server error");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
