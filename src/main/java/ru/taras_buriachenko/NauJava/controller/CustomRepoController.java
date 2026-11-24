package ru.taras_buriachenko.NauJava.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.taras_buriachenko.NauJava.entity.Message;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.criteria.CustomChatRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/custom/find")
public class CustomRepoController {

    private final CustomChatRepository customRepository;

    public CustomRepoController(CustomChatRepository customRepository) {
        this.customRepository = customRepository;
    }

    @GetMapping("/staleOnlineUsers")
    public List<User> findStaleOnlineUsers(
            @RequestParam("dateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return customRepository.findStaleOnlineUsers(dateTime);
    }

    @GetMapping("/unreadMessages")
    public List<Message> findUnreadMessages(
            @RequestParam("roomId") Long roomId,
            @RequestParam("username") String username) {
        return customRepository.findUnreadMessages(roomId, username);
    }
}
