package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.taras_buriachenko.NauJava.type.NotificationType;

@Entity
@Table(name = "notification")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

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

    @Column(name = "text")
    private String text;
}