package ru.taras_buriachenko.NauJava.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.taras_buriachenko.NauJava.dto.ParticipantResponseDto;
import ru.taras_buriachenko.NauJava.dto.RoomResponseDto;
import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.RoomRepository;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.MessageService;
import ru.taras_buriachenko.NauJava.service.ParticipantService;
import ru.taras_buriachenko.NauJava.service.RoomService;
import ru.taras_buriachenko.NauJava.type.RoomType;

import java.util.List;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final ParticipantService participantService;
    private final MessageService messageService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomController(
            RoomService roomService,
            ParticipantService participantService,
            MessageService messageService,
            RoomRepository roomRepository,
            UserRepository userRepository
    ) {
        this.roomService = roomService;
        this.participantService = participantService;
        this.messageService = messageService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/rooms")
    public String createRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String title,
            @RequestParam RoomType type,
            RedirectAttributes redirectAttributes
    ) {
        if (title == null || title.isBlank()) {
            redirectAttributes.addFlashAttribute("homeError", "Укажите название комнаты.");
            return "redirect:/home";
        }
        try {
            Room room = roomService.createRoom(title.trim(), type, currentUserId(userDetails));
            return "redirect:/rooms/" + room.getId();
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("homeError", "Не удалось создать комнату.");
            return "redirect:/home";
        }
    }

    @GetMapping("/rooms/{roomId}")
    public String roomPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long roomId,
            @RequestParam(required = false) String error,
            Model model
    ) {
        Long userId = currentUserId(userDetails);
        List<ParticipantResponseDto> participants = participantService.showAllRoomParticipants(userId, roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Комната не найдена"));

        model.addAttribute("room", new RoomResponseDto(
                room.getId(), room.getTitle(), room.getType().name(), room.getCreatedAt()));
        model.addAttribute("participants", participants);
        model.addAttribute("messages", messageService.getRoomMessages(userId, roomId));
        if (error != null) {
            model.addAttribute("chatError", "Не удалось выполнить действие. Проверьте данные и права доступа.");
        }
        return "chat";
    }

    @PostMapping("/rooms/{roomId}/messages")
    public String sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long roomId,
            @RequestParam String body
    ) {
        try {
            messageService.createMessage(currentUserId(userDetails), roomId, body);
            return "redirect:/rooms/" + roomId;
        } catch (RuntimeException exception) {
            return "redirect:/rooms/" + roomId + "?error=message";
        }
    }

    @PostMapping("/rooms/{roomId}/participants")
    public String addParticipant(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long roomId,
            @RequestParam String username
    ) {
        try {
            User invitedUser = userRepository.findByUsernameIgnoreCase(username.trim()).stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
            participantService.addParticipant(currentUserId(userDetails), roomId, invitedUser.getId());
            return "redirect:/rooms/" + roomId;
        } catch (RuntimeException exception) {
            return "redirect:/rooms/" + roomId + "?error=participant";
        }
    }

    private Long currentUserId(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"))
                .getId();
    }
}
