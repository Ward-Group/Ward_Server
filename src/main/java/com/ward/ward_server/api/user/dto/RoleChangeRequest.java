package com.ward.ward_server.api.user.dto;

import jakarta.validation.constraints.NotNull;

public record RoleChangeRequest(
        @NotNull Long userId
) {
}
