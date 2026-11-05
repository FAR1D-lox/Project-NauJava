package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public record ParticipantResponseDto (
    Long userId,
    String username,
    String roomRole,
    LocalDateTime joinedAt) {
}