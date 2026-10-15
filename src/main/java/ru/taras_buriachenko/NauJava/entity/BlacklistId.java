package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BlacklistId implements Serializable {

    public BlacklistId() {
    }

    public BlacklistId(Long initiatorId, Long blockedId) {
        this.initiatorId = initiatorId;
        this.blockedId = blockedId;
    }

    private Long initiatorId;
    private Long blockedId;

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Long getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(Long blockedId) {
        this.blockedId = blockedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o) return false;
        BlacklistId that = (BlacklistId) o;
        return Objects.equals(initiatorId, that.initiatorId) && Objects.equals(blockedId, that.blockedId);
    }

    @Override
    public int hashCode() { return Objects.hash(initiatorId, blockedId); }
}
