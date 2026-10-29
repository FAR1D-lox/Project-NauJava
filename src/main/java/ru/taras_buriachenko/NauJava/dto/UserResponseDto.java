package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public class UserResponseDto {
    private final Long id;
    private final String username;
    private final LocalDateTime lastSeen;
    private final boolean isOnline;

    public UserResponseDto(Long id, String username, LocalDateTime lastSeen, boolean isOnline) {
        this.id = id;
        this.username = username;
        this.lastSeen = lastSeen;
        this.isOnline = isOnline;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
