package ru.taras_buriachenko.NauJava.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.taras_buriachenko.NauJava.dto.RegisterRequestDto;
import ru.taras_buriachenko.NauJava.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new RegisterRequestDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegisterRequestDto dto, Model model) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            model.addAttribute("registrationError", "Username and password are required");
            return "register";
        }
        try {
            userService.addUser(dto);
            return "redirect:/login?registered";
        } catch (RuntimeException exception) {
            model.addAttribute("registrationError", "Failed to create account. Maybe username already taken.");
            return "register";
        }
    }
}
