package ru.taras_buriachenko.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.taras_buriachenko.NauJava.entity.Message;
import ru.taras_buriachenko.NauJava.entity.ParticipantId;
import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.criteria.CustomChatRepository;
import ru.taras_buriachenko.NauJava.repository.crud.MessageRepository;
import ru.taras_buriachenko.NauJava.repository.crud.ParticipantRepository;
import ru.taras_buriachenko.NauJava.repository.crud.RoomRepository;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.RoomService;
import ru.taras_buriachenko.NauJava.type.RoomType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class NauJavaApplicationTests {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final ParticipantRepository participantRepository;
    private final CustomChatRepository customChatRepository;
    private final RoomService roomService;

    @Autowired
    NauJavaApplicationTests(UserRepository userRepository,
                         RoomRepository roomRepository,
                         MessageRepository messageRepository,
                         ParticipantRepository participantRepository,
                         CustomChatRepository customChatRepository,
                         RoomService roomService) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.participantRepository = participantRepository;
        this.customChatRepository = customChatRepository;
        this.roomService = roomService;
    }

    private User createTestUser(boolean isOnline, LocalDateTime lastSeen) {
        User user = new User();
        user.setUsername("user_" + UUID.randomUUID().toString().substring(0, 8));
        user.setPasswordHash("hash_secure");
        user.setOnline(isOnline);
        user.setLastSeen(lastSeen);
        return userRepository.save(user);
    }

    private Room createTestRoom() {
        Room room = new Room();
        room.setTitle("Room_" + UUID.randomUUID().toString().substring(0, 8));
        room.setType(RoomType.GROUP);
        room.setCreatedAt(LocalDateTime.now());
        return roomRepository.save(room);
    }

    @Test
    void testQueryLookupStrategy() {
        LocalDateTime oldActivity = LocalDateTime.now().minusMinutes(10);
        User user = createTestUser(true, oldActivity);

        List<User> staleUsers = userRepository.findByIsOnlineTrueAndLastSeenLessThan(LocalDateTime.now().minusMinutes(5));

        Assertions.assertFalse(staleUsers.isEmpty());
        boolean found = staleUsers.stream().anyMatch(u -> u.getId().equals(user.getId()));
        Assertions.assertTrue(found);
    }

    @Test
    void testJpqlQuerySearchThroughRelation() {
        User sender = createTestUser(true, LocalDateTime.now());
        Room room = createTestRoom();

        Message message = new Message();
        message.setSender(sender);
        message.setRoom(room);
        message.setBody("Тестовое JPQL сообщение");
        message.setRead(false);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);

        List<Message> unread = messageRepository.findUnreadMessagesByRoomAndSender(room.getId(), sender.getUsername());

        Assertions.assertFalse(unread.isEmpty());
        Assertions.assertEquals(message.getBody(), unread.get(0).getBody());
    }

    @Test
    void testCriteriaApiStaleUsers() {
        LocalDateTime oldActivity = LocalDateTime.now().minusMinutes(15);
        User user = createTestUser(true, oldActivity);

        List<User> staleUsers = customChatRepository.findStaleOnlineUsers(LocalDateTime.now().minusMinutes(5));

        Assertions.assertFalse(staleUsers.isEmpty());
        boolean found = staleUsers.stream().anyMatch(u -> u.getId().equals(user.getId()));
        Assertions.assertTrue(found);
    }

    @Test
    void testCriteriaApiUnreadMessages() {
        User sender = createTestUser(true, LocalDateTime.now());
        Room room = createTestRoom();

        Message message = new Message();
        message.setSender(sender);
        message.setRoom(room);
        message.setBody("Тестовое Criteria сообщение");
        message.setRead(false);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);

        List<Message> unread = customChatRepository.findUnreadMessages(room.getId(), sender.getUsername());

        Assertions.assertFalse(unread.isEmpty());
        Assertions.assertEquals(message.getBody(), unread.get(0).getBody());
    }

    @Test
    void testTransactionalCreateRoomSuccess() {
        User creator = createTestUser(true, LocalDateTime.now());
        String title = "Успешная комната " + UUID.randomUUID().toString().substring(0, 5);

        // Положительный кейс: комната и связь с участником должны успешно коммититься
        Room room = roomService.createRoom(title, RoomType.GROUP, creator.getId());

        Assertions.assertNotNull(room);
        Assertions.assertNotNull(room.getId());

        ParticipantId participantId = new ParticipantId(room.getId(), creator.getId());
        Assertions.assertTrue(participantRepository.findById(participantId).isPresent());
    }

    @Test
    void testTransactionalCreateRoomRollback() {
        User creator = createTestUser(true, LocalDateTime.now());

        long roomCountBefore = roomRepository.count();
        long participantCountBefore = participantRepository.count();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            roomService.createRoom("error_room", RoomType.ONE_ON_ONE, creator.getId());
        });

        Assertions.assertEquals(roomCountBefore, roomRepository.count());
        Assertions.assertEquals(participantCountBefore, participantRepository.count());
    }
}
