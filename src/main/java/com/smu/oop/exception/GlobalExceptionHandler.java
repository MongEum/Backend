package com.smu.oop.exception;

import com.smu.oop.dto.ApiResponse;
import com.smu.oop.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getDetail() != null ?
                errorCode.getMessage() + ": " + ex.getDetail() :
                errorCode.getMessage();

        ErrorResponse error = ErrorResponse.of(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(
                ApiResponse.error("Validation failed: " + errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            Exception ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INVALID_CREDENTIALS;
        ErrorResponse error = ErrorResponse.of(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse error = ErrorResponse.of(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, errorCode.getStatus());
    }
}
