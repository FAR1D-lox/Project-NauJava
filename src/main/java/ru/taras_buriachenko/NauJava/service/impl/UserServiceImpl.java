package ru.taras_buriachenko.NauJava.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taras_buriachenko.NauJava.dto.*;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.UserService;
import ru.taras_buriachenko.NauJava.type.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public UserResponseDto addUser(RegisterRequestDto userInfo) {
        if (userRepository.existsByUsername(userInfo.getUsername())) {
            throw new EntityExistsException("User with username " + userInfo.getUsername() + " has already exists");
        }
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
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getLastSeen(),
                user.isOnline()
        );
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto update) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + userId + " not found"));
        if (update.password() != null && !update.password().isBlank())
            user.setPasswordHash(passwordEncoder.encode(update.password()));
        if (update.phoneNumber() != null && !update.phoneNumber().isBlank())
            user.setPhoneNumber(update.phoneNumber());
        if (update.username() != null && !update.username().isBlank()) {
            if (userRepository.existsByUsername(update.username()) && !update.username().equals(user.getUsername()))
                throw new EntityExistsException("User with username " + update.username() + " has already exists");
            user.setUsername(update.username());
        }
        userRepository.save(user);
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getLastSeen(),
                user.isOnline());
    }

    @Transactional
    @Override
    public void removeUser(Long userId) {
        if (userRepository.deleteUserById(userId) == 0)
            throw new EntityNotFoundException("User with id = " + userId + " not found");
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
