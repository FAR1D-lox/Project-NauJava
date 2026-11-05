package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import ru.taras_buriachenko.NauJava.type.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    public Notification() {
    }

    @EmbeddedId
    private NotificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "is_muted")
    private boolean isMuted = false;

    @Column(name = "notification_type", nullable = false)
    private NotificationType type;

    @Column(name = "message")
    private String message;

    public NotificationId getId() {
        return id;
    }

    public void setId(NotificationId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}