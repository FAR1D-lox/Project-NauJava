package ru.taras_buriachenko.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taras_buriachenko.NauJava.dto.LoginResponseDto;
import ru.taras_buriachenko.NauJava.dto.RegisterRequestDto;
import ru.taras_buriachenko.NauJava.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public LoginResponseDto register(@RequestBody RegisterRequestDto info) {
        return userService.register(info);
    }
}
