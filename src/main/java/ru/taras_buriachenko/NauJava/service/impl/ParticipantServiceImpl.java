package ru.taras_buriachenko.NauJava.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.taras_buriachenko.NauJava.dto.ParticipantResponseDto;
import ru.taras_buriachenko.NauJava.dto.RoomResponseDto;
import ru.taras_buriachenko.NauJava.entity.Participant;
import ru.taras_buriachenko.NauJava.entity.ParticipantId;
import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.ParticipantRepository;
import ru.taras_buriachenko.NauJava.repository.crud.RoomRepository;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.ParticipantService;
import ru.taras_buriachenko.NauJava.type.ParticipantRole;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public ParticipantServiceImpl(
            ParticipantRepository participantRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            PlatformTransactionManager transactionManager
    ) {
        this.participantRepository = participantRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }


    @Override
    public void addParticipant(Long currentUserId, Long roomId, Long userId) {
        ParticipantId currentParticipantId = new ParticipantId(roomId, currentUserId);
        if (!participantRepository.existsById(currentParticipantId))
            throw new SecurityException("User with id = " + currentUserId + " is not participant of room " + roomId);
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntityNotFoundException("Room with id = " + roomId + " not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User with id = " + userId + " not found"));
            ParticipantId newParticipantId = new ParticipantId(roomId, userId);

            if (!participantRepository.existsById(newParticipantId)) {
                Participant participant = new Participant();
                participant.setId(newParticipantId);
                participant.setRoom(room);
                participant.setUser(user);
                participant.setRole(ParticipantRole.DEFAULT);
                participant.setJoinedAt(LocalDateTime.now());

                participantRepository.save(participant);
            }
            transactionManager.commit(status);
        }
        catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void removeParticipant(Long currentUserId, Long roomId, Long userId) {
        ParticipantId currentParticipantId = new ParticipantId(roomId, currentUserId);
        Participant issuer = participantRepository.findById(currentParticipantId)
                .orElseThrow(() -> new SecurityException("User with id = " + currentUserId + " is not in room " + roomId));

        if (!currentUserId.equals(userId) && issuer.getRole() != ParticipantRole.ADMIN) {
            throw new SecurityException("You don't have permission to remove other participants");
        }

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            ParticipantId targetId = new ParticipantId(roomId, userId);
            if (participantRepository.existsById(targetId)) {
                participantRepository.deleteById(targetId);
            } else {
                throw new EntityNotFoundException("Participant not found in room " + roomId);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void transferAdminRole(Long currentUserId, Long roomId, Long userId) {
    }

    @Override
    public List<ParticipantResponseDto> showAllRoomParticipants(Long currentUserId, Long roomId) {
        ParticipantId currentParticipantId = new ParticipantId(roomId, currentUserId);
        if (!participantRepository.existsById(currentParticipantId)) {
            throw new SecurityException("Access denied. You are not a participant of this room.");
        }

        List<Participant> participants = participantRepository.findByIdRoomId(roomId);

        return participants.stream().map(p -> {
            User targetUser = p.getUser();

            return new ParticipantResponseDto(
                    targetUser.getId(),
                    targetUser.getUsername(),
                    p.getRole().name(),
                    p.getJoinedAt()
            );
        }).toList();
    }

    @Override
    public List<RoomResponseDto> showAllParticipantRooms(Long currentUserId) {
        List<Participant> memberships = participantRepository.findByIdUserId(currentUserId);

        return memberships.stream().map(m -> {
            Room room = m.getRoom();

            return new RoomResponseDto(
                    room.getId(),
                    room.getTitle(),
                    room.getType().name(),
                    room.getCreatedAt()
            );
        }).toList();
    }
}
