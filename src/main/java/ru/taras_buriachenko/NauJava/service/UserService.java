package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.dto.*;
import ru.taras_buriachenko.NauJava.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto addUser(RegisterRequestDto info);
    UserResponseDto updateUser(Long userId, UpdateUserDto dto);
    void removeUser(Long userId);
    List<UserResponseDto> searchUserByUsername(String username);
    List<UserResponseDto> searchUserByPhoneNumber(String phoneNumber);
}
