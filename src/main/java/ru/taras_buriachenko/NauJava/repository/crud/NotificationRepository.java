package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Notification;
import ru.taras_buriachenko.NauJava.entity.NotificationId;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, NotificationId> { }