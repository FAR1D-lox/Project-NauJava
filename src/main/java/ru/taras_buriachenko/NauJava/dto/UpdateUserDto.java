package ru.taras_buriachenko.NauJava.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class UpdateUserDto {

    public UpdateUserDto(String username, String passwordHash, String phoneNumber) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
    }

    private final String username;

    private final String passwordHash;

    private final String phoneNumber;

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
