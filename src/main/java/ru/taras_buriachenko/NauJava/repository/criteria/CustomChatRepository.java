package ru.taras_buriachenko.NauJava.repository.criteria;

import ru.taras_buriachenko.NauJava.entity.Message;
import ru.taras_buriachenko.NauJava.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomChatRepository {

    List<User> findStaleOnlineUsers(LocalDateTime dateTime);

    List<Message> findUnreadMessagesByCriteria(Long roomId, String username);
}
