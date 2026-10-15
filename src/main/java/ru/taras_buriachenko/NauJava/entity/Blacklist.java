package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklist")
public class Blacklist {

    public Blacklist() {
    }

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

    public BlacklistId getId() {
        return id;
    }

    public void setId(BlacklistId id) {
        this.id = id;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getBlocked() {
        return blocked;
    }

    public void setBlocked(User blocked) {
        this.blocked = blocked;
    }

    public LocalDateTime getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(LocalDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }
}
