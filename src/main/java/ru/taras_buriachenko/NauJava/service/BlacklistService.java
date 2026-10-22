package ru.taras_buriachenko.NauJava.service;

public interface BlacklistService {
    void banUser(Long banningUserId, Long bannedUserId);
    void unbanUser(Long banningUserId, Long bannedUserId);
}
