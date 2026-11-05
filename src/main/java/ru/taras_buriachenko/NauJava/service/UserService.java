package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.dto.*;
import ru.taras_buriachenko.NauJava.entity.User;

import java.util.List;

public interface UserService {
    LoginResponseDto register(RegisterRequestDto info);
    LoginResponseDto login(LoginRequestDto info);
    UserResponseDto updateUser(Long userId, UpdateUserDto dto);
    void removeUser(Long userId);
    List<User> searchUserByUsername(String username);
    User searchUserByPhoneNumber(String phoneNumber);
}
