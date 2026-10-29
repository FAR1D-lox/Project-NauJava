package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.dto.ParticipantResponseDto;
import ru.taras_buriachenko.NauJava.dto.RoomResponseDto;

import java.util.List;

public interface ParticipantService {
    void addParticipant(Long currentUserId, Long roomId, Long userId);
    void removeParticipant(Long currentUserId, Long roomId, Long userId);
    void transferAdminRole(Long currentUserId, Long roomId, Long userId);
    List<ParticipantResponseDto> showAllRoomParticipants(Long currentUserId, Long roomId);
    List<RoomResponseDto> showAllParticipantRooms(Long currentUserId);
}
