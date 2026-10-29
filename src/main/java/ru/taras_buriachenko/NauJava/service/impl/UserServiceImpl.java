package ru.taras_buriachenko.NauJava.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.taras_buriachenko.NauJava.dto.*;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public UserServiceImpl(UserRepository userRepository, PlatformTransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }


    @Override
    public LoginResponseDto register(RegisterRequestDto userInfo) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            User user = new User(
                    null,
                    userInfo.getUsername(),
                    userInfo.getPassword(),
                    userInfo.getPhoneNumber(),
                    LocalDateTime.now(),
                    true
            );
            userRepository.save(user);
            transactionManager.commit(status);
            return new LoginResponseDto("Какой-то токен, пока не определились какой");
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public LoginResponseDto login(LoginRequestDto info) {
        return null;
    }

    @Override
    public UserResponseDto updateUser(Long userId, UpdateUserDto dto) {
        return null;
    }

    @Override
    public void removeUser(Long userId) {

    }

    @Override
    public List<User> searchUserByUsername(String username) {
        return List.of();
    }

    @Override
    public User searchUserByPhoneNumber(String phoneNumber) {
        return null;
    }
}
