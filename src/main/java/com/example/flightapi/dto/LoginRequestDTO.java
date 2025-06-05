package com.example.flightapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;
} 