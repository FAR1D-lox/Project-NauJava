package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import ru.taras_buriachenko.NauJava.type.BlockDuration;

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

    @Column(name = "block_reason")
    private String block_reason;

    @Column(name = "block_duration")
    private BlockDuration blockDuration;

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

    public void setBlockedAt(LocalDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }

    public String getBlock_reason() {
        return block_reason;
    }

    public void setBlock_reason(String block_reason) {
        this.block_reason = block_reason;
    }

    public BlockDuration getBlockDuration() {
        return blockDuration;
    }

    public void setBlockDuration(BlockDuration blockDuration) {
        this.blockDuration = blockDuration;
    }
}