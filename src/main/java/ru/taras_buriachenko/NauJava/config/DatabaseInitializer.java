package ru.taras_buriachenko.NauJava.config;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.type.UserRole;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isPresent()) {
            throw new EntityExistsException("Administrator already exists");
        }
        //Я знаю, что подобное надо хранить в бд, но это на потом
        User admin = new User(
                null,
                "admin",
                passwordEncoder.encode("admin123"),
                "0",
                LocalDateTime.now(),
                false,
                UserRole.ADMIN
        );

        userRepository.save(admin);
        log.info("Admin created. login: admin, password: admin123");
    }
}