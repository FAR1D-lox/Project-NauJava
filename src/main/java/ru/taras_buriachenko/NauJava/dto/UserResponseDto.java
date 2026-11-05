package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public record UserResponseDto(Long id, String username, LocalDateTime lastSeen, boolean isOnline) {
}
