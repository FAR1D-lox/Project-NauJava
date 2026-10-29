package ru.taras_buriachenko.NauJava.service;

public interface ParticipantService {
    void addParticipant(Long currentUserId, Long roomId, Long userId);
    void removeParticipant(Long currentUserId, Long roomId, Long userId);
    void transferAdminRole(Long currentUserId, Long roomId, Long userId);
    void showAllRoomParticipants(Long currentUserId, Long roomId);
}
