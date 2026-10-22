package ru.taras_buriachenko.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    public CustomRepoController(CustomChatRepository customRepository) {
        this.customRepository = customRepository;
    }

    @GetMapping("/staleOnlineUsers")
    List<User> findStaleOnlineUsers(@RequestBody LocalDateTime dateTime) {
        return customRepository.findStaleOnlineUsers(dateTime);
    }

    @GetMapping("/UnreadMessage")
    List<Message> findUnreadMessages(
            @RequestBody Long roomId,
            @RequestBody String username) {
        return customRepository.findUnreadMessages(roomId, username);
    }
}
