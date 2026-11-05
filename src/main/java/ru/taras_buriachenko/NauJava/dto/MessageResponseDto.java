package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public record MessageResponseDto(
    Long id,
    Long senderId,
    String senderName,
    Long roomId,
    String body,
    String filePath,
    boolean isRead,
    LocalDateTime sentAt) {
}