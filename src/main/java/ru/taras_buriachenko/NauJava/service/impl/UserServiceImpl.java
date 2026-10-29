package ru.taras_buriachenko.NauJava.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.taras_buriachenko.NauJava.dto.*;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.UserService;
import ru.taras_buriachenko.NauJava.type.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PlatformTransactionManager transactionManager,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponseDto addUser(RegisterRequestDto userInfo) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            User user = new User(
                    null,
                    userInfo.getUsername(),
                    passwordEncoder.encode(userInfo.getPassword()),
                    userInfo.getPhoneNumber(),
                    LocalDateTime.now(),
                    true,
                    UserRole.USER
            );
            userRepository.save(user);
            transactionManager.commit(status);
            return new UserResponseDto(
                    user.getId(),
                    user.getUsername(),
                    user.getLastSeen(),
                    user.isOnline()
            );
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public UserResponseDto updateUser(Long userId, UpdateUserDto update) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + userId + " not found"));
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            if (update.getPasswordHash() != null && !update.getPasswordHash().isBlank())
                user.setPasswordHash(update.getPasswordHash());
            if (update.getPhoneNumber() != null && !update.getPhoneNumber().isBlank())
                user.setPhoneNumber(update.getPhoneNumber());
            if (update.getUsername() != null && !update.getUsername().isBlank())
                user.setUsername(update.getUsername());
            userRepository.save(user);
            transactionManager.commit(status);
            return new UserResponseDto(
                    user.getId(),
                    user.getUsername(),
                    user.getLastSeen(),
                    user.isOnline());
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void removeUser(Long userId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            if (userRepository.deleteUserById(userId) == 0)
                throw new EntityNotFoundException("User with id = " + userId + " not found");
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public List<UserResponseDto> searchUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .stream().map(user ->
                        new UserResponseDto(
                                user.getId(),
                                user.getUsername(),
                                user.getLastSeen(),
                                user.isOnline()
                        )).toList();
    }

    @Override
    public List<UserResponseDto> searchUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .stream().map(user ->
                        new UserResponseDto(
                                user.getId(),
                                user.getUsername(),
                                user.getLastSeen(),
                                user.isOnline()
                        )).toList();
    }
}
