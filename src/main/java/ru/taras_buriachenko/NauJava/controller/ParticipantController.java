package ru.taras_buriachenko.NauJava.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.taras_buriachenko.NauJava.dto.RoomResponseDto;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.ParticipantService;
import ru.taras_buriachenko.NauJava.type.RoomType;

import java.util.List;

@Controller
public class ParticipantController {

    private final ParticipantService participantService;
    private final UserRepository userRepository;

    public ParticipantController(ParticipantService participantService, UserRepository userRepository) {
        this.participantService = participantService;
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String currentUsername = userDetails.getUsername();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Long currentUserId = currentUser.getId();

        List<RoomResponseDto> myRooms = participantService.showAllParticipantRooms(currentUserId);

        model.addAttribute("rooms", myRooms);
        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("roomTypes", RoomType.values());

        return "home";
    }
}