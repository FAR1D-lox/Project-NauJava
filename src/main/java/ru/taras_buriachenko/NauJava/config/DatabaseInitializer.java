package ru.taras_buriachenko.NauJava.config;

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

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager transactionManager;

    public DatabaseInitializer(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               PlatformTransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.transactionManager = transactionManager;
    }

    @Override
    public void run(String... args) throws Exception {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            if (userRepository.findByUsername("admin").isEmpty()) {
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
                transactionManager.commit(status);
                System.out.println("Администратор по умолчанию создан. Логин: admin, Пароль: admin123");
            } else {
                transactionManager.commit(status);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            System.err.println("Не удалось создать администратора по умолчанию: " + e.getMessage());
            throw e;
        }
    }
}