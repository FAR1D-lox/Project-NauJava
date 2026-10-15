package ru.taras_buriachenko.NauJava.dto;

import java.time.LocalDateTime;

public class MessageResponseDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long roomId;
    private String body;
    private String filePath;
    private boolean isRead;
    private LocalDateTime sentAt;

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