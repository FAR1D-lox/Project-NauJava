package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Participant;
import ru.taras_buriachenko.NauJava.entity.ParticipantId;

import java.util.List;

@RepositoryRestResource(path="participants")
@Repository
public interface ParticipantRepository extends CrudRepository<Participant, ParticipantId> {

    @Modifying
    @Query("DELETE FROM Participant p WHERE p.id.roomId = :roomId")
    void deleteByRoomId(Long roomId);

    List<Participant> findByIdRoomId(Long roomId);

    List<Participant> findByIdUserId(Long userId);

}