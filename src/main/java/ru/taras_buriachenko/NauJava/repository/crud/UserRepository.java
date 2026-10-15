package ru.taras_buriachenko.NauJava.repository.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByIsOnlineTrueAndLastSeenLessThan(LocalDateTime dateTime);

    List<User> findByUsernameIgnoreCase(String username);

}
