package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
// @EqualsAndHashCode не буду использовать.
// Насколько я помню, он неправильно работает с составными ключами
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
