package ru.taras_buriachenko.NauJava.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taras_buriachenko.NauJava.dto.MessageResponseDto;
import ru.taras_buriachenko.NauJava.entity.Message;
import ru.taras_buriachenko.NauJava.entity.ParticipantId;
import ru.taras_buriachenko.NauJava.repository.crud.MessageRepository;
import ru.taras_buriachenko.NauJava.repository.crud.ParticipantRepository;
import ru.taras_buriachenko.NauJava.repository.crud.RoomRepository;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.MessageService;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Message createMessage(Long userId, Long roomId, String body) {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message can't be empty");
        }
        requireMembership(userId, roomId);
        Message message = new Message();
        message.setSender(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + userId + " not found")));
        message.setRoom(roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id = " + roomId + " not found")));
        message.setBody(body.trim());
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Transactional
    @Override
    public void removeMessage(Long userId, Long messageId) {
        Message message = findMessage(messageId);
        requireMembership(userId, message.getRoom().getId());
        if (!message.getSender().getId().equals(userId)) {
            throw new SecurityException("You can remove only your messages");
        }
        messageRepository.delete(message);
    }

    @Transactional
    @Override
    public Message editMessage(Long userId, Long messageId, String editedMessage) {
        if (editedMessage == null || editedMessage.isBlank()) {
            throw new IllegalArgumentException("Message can't be empty");
        }
        Message message = findMessage(messageId);
        requireMembership(userId, message.getRoom().getId());
        if (!message.getSender().getId().equals(userId)) {
            throw new SecurityException("You can edit only your messages");
        }
        message.setBody(editedMessage.trim());
        return messageRepository.save(message);
    }

    @Override
    public Message showMessage(Long userId, Long messageId) {
        Message message = findMessage(messageId);
        requireMembership(userId, message.getRoom().getId());
        return message;
    }

    @Override
    public List<MessageResponseDto> getRoomMessages(Long userId, Long roomId) {
        requireMembership(userId, roomId);
        return messageRepository.findRoomMessagesWithSender(roomId).stream()
                .map(message -> new MessageResponseDto(
                        message.getId(),
                        message.getSender().getId(),
                        message.getSender().getUsername(),
                        roomId,
                        message.getBody(),
                        message.getFilePath(),
                        message.isRead(),
                        message.getSentAt()
                ))
                .toList();
    }

    private Message findMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message with id = " + messageId + " not found"));
    }

    private void requireMembership(Long userId, Long roomId) {
        if (!participantRepository.existsById(new ParticipantId(roomId, userId))) {
            throw new SecurityException("You don't have access to the room");
        }
    }
}
