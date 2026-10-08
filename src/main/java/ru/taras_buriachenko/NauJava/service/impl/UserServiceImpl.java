package ru.taras_buriachenko.NauJava.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.taras_buriachenko.NauJava.config.Config;
import ru.taras_buriachenko.NauJava.repository.impl.UserRepository;
import ru.taras_buriachenko.NauJava.entity.user.User;
import ru.taras_buriachenko.NauJava.service.UserService;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Config config;

    public UserServiceImpl(UserRepository userRepository, Config config) {
        this.userRepository = userRepository;
        this.config = config;
    }

    @Override
    public void createUser(Long id, String username, String phoneNumber) {
        User user = new User(id, username, phoneNumber);
        userRepository.create(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.read(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void updateUser(Long id, String newUsername, String newPhoneNumber) {
        User currentUser = userRepository.read(id);
        if (newUsername != null && !newUsername.isEmpty()) {
            currentUser.setUsername(newUsername);
        }
        if (newPhoneNumber != null && newPhoneNumber.substring(0, 2).equals("+7") && newPhoneNumber.length() == 12) {
            currentUser.setPhoneNumber(newPhoneNumber);
        }
        userRepository.update(currentUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(id);
    }

    @PostConstruct
    public void consoleWrite() {
        System.out.println("Application name: " + config.getAppName() + "\n" +
                "Application version: " + config.getAppVersion());
    }
}
