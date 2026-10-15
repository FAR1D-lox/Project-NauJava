package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.type.ParticipantRole;
import ru.taras_buriachenko.NauJava.type.RoomType;

public interface RoomService {
    public Room createRoomWithCreator(String title, RoomType type, Long creatorId);

}
