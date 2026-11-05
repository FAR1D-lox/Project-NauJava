package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.entity.user.User;

import java.util.List;

public interface UserService {
    void createUser(Long id, String username, String phoneNumber);
    User findById(Long id);
    List<User> getAllUsers();
    void updateUser(Long id, String newUsername, String newNumber);
    void deleteById(Long id);
}
