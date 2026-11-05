package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Message;

import java.util.List;

@RepositoryRestResource(path="messages")
@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId AND m.sender.username = :username AND m.isRead = false")
    List<Message> findUnreadMessagesByRoomAndSender(
            @Param("roomId") Long roomId,
            @Param("username") String username
    );
}