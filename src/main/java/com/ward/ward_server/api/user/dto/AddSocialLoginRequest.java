package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddSocialLoginRequest(
        @NotBlank(message = "provider는 필수 항목입니다.") String provider,
        @NotBlank(message = "providerId는 필수 항목입니다.") String providerId,
        @Email(message = "유효한 이메일 주소를 입력해 주세요.") String email,
        String appleRefreshToken
) {}
