package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public class ParticipantResponseDto {
    private final Long userId;
    private final String username;
    private final String roomRole;
    private final LocalDateTime joinedAt;

    public ParticipantResponseDto(Long userId, String username, String roomRole, LocalDateTime joinedAt) {
        this.userId = userId;
        this.username = username;
        this.roomRole = roomRole;
        this.joinedAt = joinedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRoomRole() {
        return roomRole;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}