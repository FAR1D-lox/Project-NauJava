package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.entity.Message;

public interface MessageService {
    Message createMessage(Long userId, Long roomId, String message);
    void removeMessage(Long userId, Long messageId);
    Message editMessage(Long userId, Long messageId, String editedMessage);
    Message showMessage(Long userId, Long messageId);
}
