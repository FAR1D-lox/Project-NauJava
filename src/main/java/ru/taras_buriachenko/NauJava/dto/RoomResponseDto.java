package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public class RoomResponseDto {
    private final Long id;
    private final String title;
    private final String type;
    private final LocalDateTime createdAt;


    public RoomResponseDto(Long id, String title, String type, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
