package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameRequest(
        @NotBlank(message = "닉네임은 필수 값입니다.")
        String newNickname
) {}
