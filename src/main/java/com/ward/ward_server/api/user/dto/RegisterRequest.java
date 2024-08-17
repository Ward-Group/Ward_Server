package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "provider는 필수 항목입니다.") String provider,
        @NotBlank(message = "providerId는 필수 항목입니다.") String providerId,
        @NotBlank(message = "이름은 필수 항목입니다.") String name,
        @Email(message = "유효한 이메일 주소를 입력해 주세요.") String email,
        String appleRefreshToken,
        @NotBlank(message = "닉네임은 필수 항목입니다.") String nickname,
        Boolean emailNotification,
        Boolean appPushNotification,
        Boolean snsNotification
) {
    public RegisterRequest {
        if (emailNotification == null) emailNotification = false;
        if (appPushNotification == null) appPushNotification = false;
        if (snsNotification == null) snsNotification = false;
    }
}
