package com.smu.oop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank(message = "Nickname is required")
    @Size(min = 2, max = 10, message = "Nickname must be between 2 and 10 characters")
    private String nickname;

    @NotBlank(message = "Email is required")
    @Size(min = 2, max = 64, message = "Email must be between 2 and 64 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
