package ru.taras_buriachenko.NauJava.dto;

import java.util.List;

public record ReportData(
        long usersCount,
        long usersElapsedMs,
        long roomsElapsedMs,
        long totalElapsedMs,
        String generatedAt,
        List<RoomRow> rooms
) {
    public record RoomRow(Long id, String title, String type, String createdAt) {
    }
}