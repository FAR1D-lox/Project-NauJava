package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.taras_buriachenko.NauJava.type.ParticipantRole;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "participants")
public class Participant {


    @EmbeddedId
    private ParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    private ParticipantRole role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "room_custom_username")
    private String roomCustomUsername;

}