package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.type.RoomType;

public interface RoomService {
    Room createRoom(String title, RoomType type, Long creatorId);
    void removeRoom(Long userId, Long roomId);
}
