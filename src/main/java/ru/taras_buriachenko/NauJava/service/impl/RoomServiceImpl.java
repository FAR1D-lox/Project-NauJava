package ru.taras_buriachenko.NauJava.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.taras_buriachenko.NauJava.entity.Participant;
import ru.taras_buriachenko.NauJava.entity.ParticipantId;
import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.repository.crud.ParticipantRepository;
import ru.taras_buriachenko.NauJava.repository.crud.RoomRepository;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.service.RoomService;
import ru.taras_buriachenko.NauJava.type.ParticipantRole;
import ru.taras_buriachenko.NauJava.type.RoomType;

import java.time.LocalDateTime;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public RoomServiceImpl(RoomRepository roomRepository,
                           ParticipantRepository participantRepository,
                           UserRepository userRepository,
                           PlatformTransactionManager transactionManager
    ) {
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;;
    }

    @Override
    public Room createRoomWithCreator(String title, RoomType type, Long creatorId) {

        TransactionStatus status =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
        Room savedRoom = null;
        try {
            Room room = new Room();
            room.setTitle(title);
            room.setType(type);
            room.setCreatedAt(LocalDateTime.now());
            savedRoom = roomRepository.save(room);

            if ("error_room".equals(title)) {
                throw new IllegalArgumentException("Invalid room title. Transaction rollback expected.");
            }

            User creator = userRepository.findById(creatorId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + creatorId));

            ParticipantId participantId = new ParticipantId(savedRoom.getId(), creator.getId());
            Participant participant = new Participant();
            participant.setId(participantId);
            participant.setRoom(savedRoom);
            participant.setUser(creator);
            participant.setRole(ParticipantRole.CREATOR);
            participant.setJoinedAt(LocalDateTime.now());

            participantRepository.save(participant);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }

        return savedRoom;
    }
}
