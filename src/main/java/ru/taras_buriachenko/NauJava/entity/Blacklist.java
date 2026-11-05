package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.taras_buriachenko.NauJava.type.BlockDuration;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklist")
@Setter
@Getter
@NoArgsConstructor
public class Blacklist {

    @EmbeddedId
    private BlacklistId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("initiatorId")
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("blockedId")
    @JoinColumn(name = "blocked_id")
    private User blocked;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "block_reason")
    private String block_reason;

    @Column(name = "block_duration")
    private BlockDuration blockDuration;
}