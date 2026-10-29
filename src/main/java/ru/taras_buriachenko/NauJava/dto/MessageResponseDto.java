package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public class MessageResponseDto {
    private final Long id;
    private final Long senderId;
    private final String senderName;
    private final Long roomId;
    private final String body;
    private final String filePath;
    private final boolean isRead;
    private final LocalDateTime sentAt;

    public MessageResponseDto(Long id, Long senderId, String senderName, Long roomId, String body, String filePath, boolean isRead, LocalDateTime sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.roomId = roomId;
        this.body = body;
        this.filePath = filePath;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getBody() {
        return body;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}