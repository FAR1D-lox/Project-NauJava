package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Room;

@RepositoryRestResource(path="rooms")
@Repository
public interface RoomRepository extends CrudRepository<Room, Long> { }