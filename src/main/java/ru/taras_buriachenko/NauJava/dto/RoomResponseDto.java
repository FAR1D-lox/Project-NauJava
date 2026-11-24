package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public record RoomResponseDto(
    Long id,
    String title,
    String type,
    LocalDateTime createdAt) {
}
