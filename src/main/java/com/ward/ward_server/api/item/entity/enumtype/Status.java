package com.ward.ward_server.api.item.entity.enumtype;

import java.time.LocalDateTime;

public enum Status {
    POSSIBLE, IMPOSSIBLE, ALWAYS;

    public static Status of(LocalDateTime releaseDate, LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        if (dueDate == null && now.isAfter(releaseDate)) return ALWAYS;
        else if (dueDate != null && now.isAfter(dueDate)) return IMPOSSIBLE;
        else return POSSIBLE;
    }
}
