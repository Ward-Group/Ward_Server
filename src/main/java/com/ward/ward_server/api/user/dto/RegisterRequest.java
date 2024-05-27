package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Provider cannot be empty")
    private String provider;

    @NotBlank(message = "Provider ID cannot be empty")
    private String providerId;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Nickname cannot be empty")
    private String nickname;

    private Boolean emailNotification;
    private Boolean appPushNotification;
    private Boolean snsNotification;
}
