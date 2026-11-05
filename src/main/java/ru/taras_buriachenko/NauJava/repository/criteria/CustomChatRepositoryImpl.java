package ru.taras_buriachenko.NauJava.repository.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.Message;
import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomChatRepositoryImpl implements CustomChatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findStaleOnlineUsers(LocalDateTime dateTime) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        Predicate isTrue = cb.isTrue(user.get("isOnline"));
        Predicate isBefore = cb.lessThan(user.get("lastSeen"), dateTime);

        query.select(user).where(cb.and(isTrue, isBefore));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Message> findUnreadMessagesByCriteria(Long roomId, String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> query = cb.createQuery(Message.class);
        Root<Message> message = query.from(Message.class);

        Join<Message, Room> roomJoin = message.join("room");
        Join<Message, User> senderJoin = message.join("sender");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(roomJoin.get("id"), roomId));
        predicates.add(cb.equal(senderJoin.get("username"), username));
        predicates.add(cb.isFalse(message.get("isRead")));

        query.select(message).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
