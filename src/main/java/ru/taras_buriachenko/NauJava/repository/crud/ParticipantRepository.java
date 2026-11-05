package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Participant;
import ru.taras_buriachenko.NauJava.entity.ParticipantId;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, ParticipantId> { }