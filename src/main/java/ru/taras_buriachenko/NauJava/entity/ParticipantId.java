package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipantId implements Serializable {

    public ParticipantId() {
    }

    public ParticipantId(Long roomId, Long userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    private Long roomId;

    private Long userId;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o) return false;
        ParticipantId that = (ParticipantId) o;
        return Objects.equals(roomId, that.roomId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() { return Objects.hash(roomId, userId); }
}
