package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
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
