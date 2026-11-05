package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotificationId implements Serializable {

    public NotificationId() {

    }

    public NotificationId(Long userId, Long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    private Long userId;
    private Long roomId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o) return false;
        NotificationId that = (NotificationId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() { return Objects.hash(userId, roomId); }
}
