package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddSocialLoginRequest(
        @NotBlank(message = "Provider cannot be empty") String provider,
        @NotBlank(message = "Provider ID cannot be empty") String providerId,
        @Email(message = "Email should be valid") String email,
        String appleRefreshToken
) {}
