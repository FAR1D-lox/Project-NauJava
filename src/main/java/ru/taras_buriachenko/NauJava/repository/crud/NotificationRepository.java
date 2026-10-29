package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Notification;
import ru.taras_buriachenko.NauJava.entity.NotificationId;

@RepositoryRestResource(path="notifications")
@Repository
public interface NotificationRepository extends CrudRepository<Notification, NotificationId> { }