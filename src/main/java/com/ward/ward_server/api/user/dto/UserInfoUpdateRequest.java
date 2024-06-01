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
public class UserInfoUpdateRequest {
    @NotBlank(message = "Provider cannot be empty")
    private String provider;

    @NotBlank(message = "Provider ID cannot be empty")
    private String providerId;

    @Email(message = "Email should be valid")
    private String oldEmail;

    @Email(message = "Email should be valid")
    private String newEmail;
}
