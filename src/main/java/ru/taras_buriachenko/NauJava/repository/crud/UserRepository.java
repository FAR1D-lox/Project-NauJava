package ru.taras_buriachenko.NauJava.repository.crud;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.taras_buriachenko.NauJava.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path="users")
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByIsOnlineTrueAndLastSeenLessThan(LocalDateTime dateTime);

    List<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsername(String username);

    @Modifying
    long deleteUserById(Long id);

    List<User> findByPhoneNumber(String phoneNumber);
}
