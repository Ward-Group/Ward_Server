package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Provider cannot be empty") String provider,
        @NotBlank(message = "Provider ID cannot be empty") String providerId,
        @NotBlank(message = "Name cannot be empty") String name,
        @Email(message = "Email should be valid") String email,
        @NotBlank(message = "Nickname cannot be empty") String nickname,
        Boolean emailNotification,
        Boolean appPushNotification,
        Boolean snsNotification
) {}
